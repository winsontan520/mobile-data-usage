package com.winsontan520.mobiledatausage.data.remote

import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import retrofit2.HttpException
import java.net.HttpURLConnection

class MobileDataUsageServiceTest : BaseTest() {
    @Test
    fun `get all mobile data usages`() {
        mockHttpResponse(mockServer, "mobile_data_usages.json", HttpURLConnection.HTTP_OK)
        runBlocking {
            val apiResult = mobileDataUsageService.fetchMobileUsageDataAsync().await()
            Assert.assertEquals(true, apiResult.success)
            Assert.assertEquals(59, apiResult.result.total)
            Assert.assertEquals(5, apiResult.result.records.size)
            Assert.assertEquals(5, apiResult.result.limit)
        }
    }

    @Test(expected = HttpException::class)
    fun `get all mobile data usages and fail`() {
        mockHttpResponse(mockServer, "mobile_data_usages.json", HttpURLConnection.HTTP_FORBIDDEN)
        runBlocking {
            mobileDataUsageService.fetchMobileUsageDataAsync().await()
        }
    }
}