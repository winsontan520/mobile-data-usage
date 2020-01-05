package com.winsontan520.mobiledatausage.data.remote

import com.winsontan520.mobiledatausage.data.model.ApiResult
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface MobileDataUsageService {

    @GET("api/action/datastore_search")
    fun fetchMobileUsageDataAsync(@Query("resource_id") query: String = "a807b7ab-6cad-4aa6-87d0-e283a7353a0f"): Deferred<ApiResult>

}