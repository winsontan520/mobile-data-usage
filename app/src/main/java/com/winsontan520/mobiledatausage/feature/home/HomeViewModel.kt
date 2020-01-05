package com.winsontan520.mobiledatausage.feature.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.winsontan520.mobiledatausage.R
import com.winsontan520.mobiledatausage.common.base.BaseViewModel
import com.winsontan520.mobiledatausage.common.utils.Event
import com.winsontan520.mobiledatausage.data.model.MobileDataUsage
import com.winsontan520.mobiledatausage.data.repository.AppDispatchers
import com.winsontan520.mobiledatausage.data.repository.utils.Resource
import com.winsontan520.mobiledatausage.feature.home.domain.GetMobileDataUsageUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val getMobileDataUsageUseCase: GetMobileDataUsageUseCase,
    private val dispatchers: AppDispatchers
) : BaseViewModel() {

    private val _mobileDataUsages = MediatorLiveData<Resource<List<MobileDataUsage>>>()
    val mobileDataUsages: LiveData<Resource<List<MobileDataUsage>>> get() = _mobileDataUsages
    private var mobileDataUsagesSource: LiveData<Resource<List<MobileDataUsage>>> =
        MutableLiveData()
    var toastMessage = MutableLiveData<Int?>()

    init {
        getMobileDataUsages(false)
    }

    fun onClickItem(mobileDataUsage: MobileDataUsage) {
        toastMessage.value = R.string.on_click_item
//        navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment("detail"))
    }


    fun onClickImage(mobileDataUsage: MobileDataUsage) {
        toastMessage.value = R.string.on_click_image
    }

    fun userRefreshesItems() = getMobileDataUsages(true)

    private fun getMobileDataUsages(forceRefresh: Boolean) =
        viewModelScope.launch(dispatchers.main) {
            _mobileDataUsages.removeSource(mobileDataUsagesSource)
            withContext(dispatchers.io) {
                mobileDataUsagesSource = getMobileDataUsageUseCase(forceRefresh = forceRefresh)
            }
            _mobileDataUsages.addSource(mobileDataUsagesSource) {
                _mobileDataUsages.value = it
                if (it.status == Resource.Status.ERROR) _snackbarError.value =
                    Event(R.string.an_error_happened)
            }
        }
}