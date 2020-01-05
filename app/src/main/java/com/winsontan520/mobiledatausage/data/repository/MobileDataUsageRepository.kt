package com.winsontan520.mobiledatausage.data.repository

import androidx.lifecycle.LiveData
import com.winsontan520.mobiledatausage.data.local.dao.RecordDao
import com.winsontan520.mobiledatausage.data.model.ApiResult
import com.winsontan520.mobiledatausage.data.model.Record
import com.winsontan520.mobiledatausage.data.remote.MobileDataUsageDataSource
import com.winsontan520.mobiledatausage.data.repository.utils.NetworkBoundResource
import com.winsontan520.mobiledatausage.data.repository.utils.Resource
import kotlinx.coroutines.Deferred

interface MobileDataUsageRepository {
    suspend fun getMobileDataUsagesWithCache(forceRefresh: Boolean = false): LiveData<Resource<List<Record>>>
}

class MobileDataUsageRepositoryImpl(
    private val datasource: MobileDataUsageDataSource,
    private val dao: RecordDao
) : MobileDataUsageRepository {
    override suspend fun getMobileDataUsagesWithCache(forceRefresh: Boolean): LiveData<Resource<List<Record>>> {

        return object : NetworkBoundResource<List<Record>, ApiResult>() {

            override fun processResponse(response: ApiResult): List<Record> =
                response.result.records

            override suspend fun saveCallResults(items: List<Record>) = dao.save(items)

            override fun shouldFetch(data: List<Record>?): Boolean =
                data == null || data.isEmpty() || forceRefresh

            override suspend fun loadFromDb(): List<Record> = dao.getAllRecords()

            override fun createCallAsync(): Deferred<ApiResult> = datasource.fetchMobileDataUsageAsync()

        }.build().asLiveData()
    }

}
