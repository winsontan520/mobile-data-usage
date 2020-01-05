package com.winsontan520.mobiledatausage.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.winsontan520.mobiledatausage.common.CoroutinesMainDispatcherRule
import com.winsontan520.mobiledatausage.data.local.dao.RecordDao
import com.winsontan520.mobiledatausage.data.model.ApiResult
import com.winsontan520.mobiledatausage.data.model.Record
import com.winsontan520.mobiledatausage.data.remote.MobileDataUsageDataSource
import com.winsontan520.mobiledatausage.data.repository.utils.Resource
import com.winsontan520.mobiledatausage.mock.FakeData
import io.mockk.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

import org.junit.Rule

class MobileDataUsageRepositoryTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesMainDispatcherRule = CoroutinesMainDispatcherRule()

    // FOR DATA
    private lateinit var observer: Observer<Resource<List<Record>>>
    private lateinit var observerRecord: Observer<Resource<Record>>
    private lateinit var repository: MobileDataUsageRepository
    private val service = mockk<MobileDataUsageDataSource>()
    private val recordDao = mockk<RecordDao>(relaxed = true)

    @Before
    fun setUp() {
        observer = mockk(relaxed = true)
        observerRecord = mockk(relaxed = true)
        repository = MobileDataUsageRepositoryImpl(service, recordDao)
    }


    @Test
    fun `Get mobile data usages from server when no internet is available`() {
        val exception = Exception("Internet")
        every { service.fetchMobileDataUsageAsync() } throws exception
        coEvery { recordDao.getAllRecords() } returns listOf()

        runBlocking {
            repository.getMobileDataUsagesWithCache().observeForever(observer)
        }

        verifyOrder {
            observer.onChanged(Resource.loading(null)) // Init loading with no value
            observer.onChanged(Resource.loading(listOf())) // Then trying to load from db (fast temp loading) before load from remote source
            observer.onChanged(Resource.error(exception, listOf())) // Retrofit 403 error
        }
        confirmVerified(observer)
    }

    @Test
    fun `Get mobile data usages from network`() {
        val fakeResult = FakeData.createFakeResult(5)
        every { service.fetchMobileDataUsageAsync() } returns GlobalScope.async {
            ApiResult(
                help = "fake",
                result = fakeResult,
                success = true
            )
        }
        coEvery { recordDao.getAllRecords() } returns listOf() andThen { fakeResult.records }

        runBlocking {
            repository.getMobileDataUsagesWithCache().observeForever(observer)
        }

        verifyOrder {
            observer.onChanged(Resource.loading(null)) // Loading from remote source
            observer.onChanged(Resource.loading(listOf())) // Then trying to load from db (fast temp loading) before load from remote source
            observer.onChanged(Resource.success(fakeResult.records)) // Success
        }

        coVerify(exactly = 1) {
            recordDao.save(fakeResult.records)
        }

        confirmVerified(observer)
    }

    @Test
    fun `Get mobile usages from db`() {
        val fakeResult = FakeData.createFakeResult(5)
        every { service.fetchMobileDataUsageAsync() } returns GlobalScope.async {
            ApiResult(
                help = "fake",
                result = fakeResult,
                success = true
            )
        }
        coEvery { recordDao.getAllRecords() } returns fakeResult.records

        runBlocking {
            repository.getMobileDataUsagesWithCache().observeForever(observer)
        }

        verifyOrder {
            observer.onChanged(Resource.loading(null)) // Loading from remote source
            observer.onChanged(Resource.success(fakeResult.records)) // Success
        }

        coVerify(exactly = 0) {
            recordDao.save(fakeResult.records)
        }

        confirmVerified(observer)
    }
}