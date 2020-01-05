package com.winsontan520.mobiledatausage.feature.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.test.filters.SmallTest
import com.winsontan520.mobiledatausage.common.test.RecordDataSet.FAKE_MOBILE_DATA_USAGES
import com.winsontan520.mobiledatausage.common.test.RecordDataSet.FAKE_MOBILE_DATA_USAGES_OLD
import com.winsontan520.mobiledatausage.common.utils.Event
import com.winsontan520.mobiledatausage.data.model.MobileDataUsage
import com.winsontan520.mobiledatausage.data.repository.AppDispatchers
import com.winsontan520.mobiledatausage.data.repository.utils.Resource
import com.winsontan520.mobiledatausage.feature.home.domain.GetMobileDataUsageUseCase
import io.mockk.coEvery
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
@SmallTest
class HomeUnitTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var getMobileDataUsageUseCase: GetMobileDataUsageUseCase
    private lateinit var homeViewModel: HomeViewModel
    private val appDispatchers = AppDispatchers(Dispatchers.Unconfined, Dispatchers.Unconfined)

    @Before
    fun setUp() {
        getMobileDataUsageUseCase = mockk()
    }

    @Test
    fun `Mobile data usage requested when ViewModel is created`() {
        val observer = mockk<Observer<Resource<List<MobileDataUsage>>>>(relaxed = true)
        val result = Resource.success(FAKE_MOBILE_DATA_USAGES)
        coEvery { getMobileDataUsageUseCase(false) } returns MutableLiveData<Resource<List<MobileDataUsage>>>().apply {
            value = result
        }

        homeViewModel = HomeViewModel(getMobileDataUsageUseCase, appDispatchers)
        homeViewModel.mobileDataUsages.observeForever(observer)

        verify {
            observer.onChanged(result)
        }

        confirmVerified(observer)
    }

    @Test
    fun `Mobile data usage requested but failed when ViewModel is created`() {
        val observer = mockk<Observer<Resource<List<MobileDataUsage>>>>(relaxed = true)
        val observerSnackbar = mockk<Observer<Event<Int>>>(relaxed = true)
        val result = Resource.error(Exception("fail"), null)
        coEvery { getMobileDataUsageUseCase(any()) } returns MutableLiveData<Resource<List<MobileDataUsage>>>().apply {
            value = result
        }

        homeViewModel = HomeViewModel(getMobileDataUsageUseCase, appDispatchers)
        homeViewModel.mobileDataUsages.observeForever(observer)
        homeViewModel.snackBarError.observeForever(observerSnackbar)

        verify {
            observer.onChanged(result)
            observerSnackbar.onChanged(homeViewModel.snackBarError.value)
        }

        confirmVerified(observer)
    }

    @Test
    fun `Mobile data usage 2 times clicks on item on RecyclerView`() {
        val observer = mockk<Observer<Int?>>(relaxed = true)
        coEvery { getMobileDataUsageUseCase(any()) } returns MutableLiveData<Resource<List<MobileDataUsage>>>().apply {
            value = Resource.success(FAKE_MOBILE_DATA_USAGES)
        }
        homeViewModel = HomeViewModel(getMobileDataUsageUseCase, appDispatchers)
        homeViewModel.toastMessage.observeForever(observer)
        homeViewModel.onClickItem(FAKE_MOBILE_DATA_USAGES.first())
        homeViewModel.onClickItem(FAKE_MOBILE_DATA_USAGES.first())

        verify(exactly = 2) {
            observer.onChanged(homeViewModel.toastMessage.value)
        }

        confirmVerified(observer)
    }

    @Test
    fun `Mobile data usages refreshes list with swipe to refresh`() {
        val observer = mockk<Observer<Resource<List<MobileDataUsage>>>>(relaxed = true)
        val resultWithoutForce = Resource.success(FAKE_MOBILE_DATA_USAGES_OLD)
        val resultWithForce = Resource.success(FAKE_MOBILE_DATA_USAGES)
        coEvery { getMobileDataUsageUseCase(false) } returns MutableLiveData<Resource<List<MobileDataUsage>>>().apply {
            value = resultWithoutForce
        }
        coEvery { getMobileDataUsageUseCase(true) } returns MutableLiveData<Resource<List<MobileDataUsage>>>().apply {
            value = resultWithForce
        }

        homeViewModel = HomeViewModel(getMobileDataUsageUseCase, appDispatchers)
        homeViewModel.mobileDataUsages.observeForever(observer)
        homeViewModel.userRefreshesItems()

        verify {
            observer.onChanged(resultWithoutForce)
            observer.onChanged(resultWithForce)
        }

        confirmVerified(observer)
    }
}