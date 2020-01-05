package com.winsontan520.mobiledatausage.feature.home

import android.view.View
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.winsontan520.mobiledatausage.R
import com.winsontan520.mobiledatausage.common.test.RecordDataSet
import com.winsontan520.mobiledatausage.data.model.Record
import com.winsontan520.mobiledatausage.data.repository.AppDispatchers
import com.winsontan520.mobiledatausage.data.repository.MobileDataUsageRepository
import com.winsontan520.mobiledatausage.data.repository.utils.Resource
import com.winsontan520.mobiledatausage.feature.home.domain.GetMobileDataUsageUseCase
import com.winsontan520.mobiledatausage.util.RecyclerViewItemCountAssertion.Companion.withItemCount
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest


@RunWith(AndroidJUnit4::class)
@LargeTest
class HomeInstrumentedTest : KoinTest {

    private val mobileDataUsageRepository = mockk<MobileDataUsageRepository>()
    private val featureHomeModule = module(override = true) {
        factory { GetMobileDataUsageUseCase(get()) }
        viewModel { HomeViewModel(get(), get()) }
    }

    private val repositoryModule = module(override = true) {
        factory { AppDispatchers(Dispatchers.Main, Dispatchers.IO) }
        factory { mobileDataUsageRepository }
    }

    @Before
    fun setUp() {
        loadKoinModules(listOf(featureHomeModule, repositoryModule))
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun testRecyclerViewContainsItems() {
        coEvery { mobileDataUsageRepository.getMobileDataUsagesWithCache(false) } returns MutableLiveData<Resource<List<Record>>>().apply {
            postValue(Resource.success(RecordDataSet.FAKE_RECORDS))
        }

        launchFragment()

        onView(withId(R.id.fragment_home_rv))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
        onView(withId(R.id.fragment_home_rv)).check(withItemCount(2))
    }

    @Test
    fun testScreenBehaviorWhenError() {
        coEvery { mobileDataUsageRepository.getMobileDataUsagesWithCache(false) } returns MutableLiveData<Resource<List<Record>>>().apply {
            postValue(
                Resource.error(
                    Exception("error"), listOf()
                )
            )
        }

        launchFragment()

        onView(withId(R.id.fragment_home_text_view_empty_list)).check(
            matches(
                isDisplayed()
            )
        )
        onView(withId(com.google.android.material.R.id.snackbar_text)).check(
            matches(withText(R.string.an_error_happened))
        )
    }

    @Test
    fun testRefreshWhenError() {
        coEvery { mobileDataUsageRepository.getMobileDataUsagesWithCache(any()) } returns MutableLiveData<Resource<List<Record>>>().apply {
            postValue(
                Resource.error(
                    Exception("no_internet"), RecordDataSet.FAKE_RECORDS
                )
            )
        }

        launchFragment()

        onView(withId(R.id.fragment_home_swipe_to_refresh)).perform(
            ViewActions.swipeDown()
        )

        onView(withId(com.google.android.material.R.id.snackbar_text)).check(
            matches(withText(R.string.an_error_happened))
        )
        onView(withId(R.id.fragment_home_rv)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0)
        )

        onView(withId(R.id.fragment_home_rv)).check(withItemCount(2))
    }

    @Test
    fun testImageShowWhenRecordHaveDeclineInQuarter() {
        coEvery { mobileDataUsageRepository.getMobileDataUsagesWithCache(false) } returns MutableLiveData<Resource<List<Record>>>().apply {
            postValue(Resource.success(RecordDataSet.FAKE_RECORDS_WITH_DECLINE))
        }
        launchFragment()

        // position 0 is year 2005 which isDeclining and should show image
        onView(withId(R.id.fragment_home_rv))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
            .check(matches(atPositionOnView(0, isDisplayed(), R.id.iv_decline)))

        // position 1 is year 2004 which not declining and should not show image
        onView(withId(R.id.fragment_home_rv))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(1))
            .check(matches(atPositionOnView(1, not(isDisplayed()), R.id.iv_decline)))
    }

    @Test
    fun testNoImageShowForNonDecliningYear() {
        coEvery { mobileDataUsageRepository.getMobileDataUsagesWithCache(false) } returns MutableLiveData<Resource<List<Record>>>().apply {
            postValue(Resource.success(RecordDataSet.FAKE_RECORDS))
        }
        launchFragment()

        // position 0 and 1 show not show any image
        onView(withId(R.id.fragment_home_rv))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
            .check(matches(atPositionOnView(0, not(isDisplayed()), R.id.iv_decline)))

        onView(withId(R.id.fragment_home_rv))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(1))
            .check(matches(atPositionOnView(1, not(isDisplayed()), R.id.iv_decline)))
    }

    private fun atPositionOnView(
        position: Int, itemMatcher: Matcher<View>,
        targetViewId: Int
    ): Matcher<View> {

        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has view id $itemMatcher at position $position")
            }

            public override fun matchesSafely(recyclerView: RecyclerView): Boolean {
                val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
                val targetView = viewHolder!!.itemView.findViewById<View>(targetViewId)
                return itemMatcher.matches(targetView)
            }
        }
    }

    private fun launchFragment(): NavController {
        val mockNavController = mockk<NavController>(relaxed = true)
        val homeScenario =
            launchFragmentInContainer<HomeFragment>(themeResId = R.style.AppTheme)
        homeScenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
        }
        return mockNavController
    }
}