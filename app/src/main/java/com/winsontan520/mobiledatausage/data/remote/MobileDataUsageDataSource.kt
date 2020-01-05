package com.winsontan520.mobiledatausage.data.remote

class MobileDataUsageDataSource (private val mobileDataUsageService: MobileDataUsageService) {

    fun fetchMobileDataUsageAsync() = mobileDataUsageService.fetchMobileUsageDataAsync()
    
}