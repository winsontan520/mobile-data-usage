package com.winsontan520.mobiledatausage.feature.home.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.winsontan520.mobiledatausage.common.test.RecordDataSet
import com.winsontan520.mobiledatausage.data.model.MobileDataUsage
import com.winsontan520.mobiledatausage.data.model.Record
import com.winsontan520.mobiledatausage.data.repository.MobileDataUsageRepository
import com.winsontan520.mobiledatausage.data.repository.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class GetMobileDataUsageUseCaseTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var useCase: GetMobileDataUsageUseCase
    private lateinit var mobileDataUsageRepository: MobileDataUsageRepository

    @Before
    fun setUp() {
        mobileDataUsageRepository = mockk()
    }


    @Test
    fun `GetMobileDataUsageUseCaseTest`() = runBlocking {
        val record1 = Record(id = 1, quarter = "2004-Q3", volumeOfMobileData = "0.1")
        val record2 = Record(id = 2, quarter = "2004-Q4", volumeOfMobileData = "0.2")
        val record3 = Record(id = 3, quarter = "2005-Q1", volumeOfMobileData = "0.3")
        val record4 = Record(id = 4, quarter = "2005-Q2", volumeOfMobileData = "0.4")
        val record5 = Record(id = 5, quarter = "2005-Q3", volumeOfMobileData = "0.1")
        val mockRecords = Resource.success(listOf(record1, record2, record3, record4, record5))
        coEvery { mobileDataUsageRepository.getMobileDataUsagesWithCache(any()) } returns MutableLiveData<Resource<List<Record>>>().apply {
            value = mockRecords
        }

        // list object descending by year
        val expectedMobileDataUsage = listOf(
            MobileDataUsage(
                year = 2005,
                volumeOfMobileDataInYear = record3.volumeOfMobileData.toDouble() + record4.volumeOfMobileData.toDouble() + record5.volumeOfMobileData.toDouble(),
                recordQ1 = record3,
                recordQ2 = record4,
                recordQ3 = record5,
                recordQ4 = null,
                isDecline = true // true if current year have decline volume in quarter
            ),
            MobileDataUsage(
                year = 2004,
                volumeOfMobileDataInYear = record1.volumeOfMobileData.toDouble() + record2.volumeOfMobileData.toDouble(),
                recordQ1 = null,
                recordQ2 = null,
                recordQ3 = record1,
                recordQ4 = record2,
                isDecline = false
            )
        )
        val expectedResult = Resource.success(expectedMobileDataUsage)
        val observer = mockk<Observer<Resource<List<MobileDataUsage>>>>(relaxed = true)
        useCase = GetMobileDataUsageUseCase(mobileDataUsageRepository)
        val liveData = useCase.invoke(false)
        liveData.observeForever(observer)

        verify {
            observer.onChanged(expectedResult)
        }
    }
}