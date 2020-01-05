package com.winsontan520.mobiledatausage.feature.home.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.winsontan520.mobiledatausage.data.model.MobileDataUsage
import com.winsontan520.mobiledatausage.data.repository.MobileDataUsageRepository
import com.winsontan520.mobiledatausage.data.repository.utils.Resource

class GetMobileDataUsageUseCase(private val repository: MobileDataUsageRepository) {

    suspend operator fun invoke(forceRefresh: Boolean = false): LiveData<Resource<List<MobileDataUsage>>> {
        return Transformations.map(repository.getMobileDataUsagesWithCache(forceRefresh)) {
            var mobileDataUsagesMap: HashMap<String, MobileDataUsage> = HashMap()

            val records = it.data
            records?.forEach { record ->
                // format YYYY-QQ
                val year = record.quarter.substring(0, 4)
                val quarter = record.quarter.substring(5, 7)
                if (!mobileDataUsagesMap.containsKey(year)) {
                    mobileDataUsagesMap[year] = MobileDataUsage(year.toInt())
                }

                val mobileDataUsage = mobileDataUsagesMap[year]
                when (quarter) {
                    "Q1" -> mobileDataUsage?.recordQ1 = record
                    "Q2" -> mobileDataUsage?.recordQ2 = record
                    "Q3" -> mobileDataUsage?.recordQ3 = record
                    "Q4" -> mobileDataUsage?.recordQ4 = record
                }

                mobileDataUsage?.isDecline = isDeclining(mobileDataUsage)
                mobileDataUsage?.volumeOfMobileDataInYear = calculateVolumeInYear(mobileDataUsage)
            }

            val mobileDataUsages = mutableListOf<MobileDataUsage>()

            mobileDataUsagesMap.map { entry ->
                mobileDataUsages.add(entry.value)
            }
            mobileDataUsages.sortByDescending { it.year }
            val result = Resource(it.status, mobileDataUsages, it.error)
            result
        }
    }

    private fun calculateVolumeInYear(mobileDataUsage: MobileDataUsage?): Double {
        var total = 0.0;
        mobileDataUsage?.recordQ1?.volumeOfMobileData?.toDouble()?.let { total += it }
        mobileDataUsage?.recordQ2?.volumeOfMobileData?.toDouble()?.let { total += it }
        mobileDataUsage?.recordQ3?.volumeOfMobileData?.toDouble()?.let { total += it }
        mobileDataUsage?.recordQ4?.volumeOfMobileData?.toDouble()?.let { total += it }
        return total
    }

    private fun isDeclining(mobileDataUsage: MobileDataUsage?): Boolean {
        val q1: Double? = mobileDataUsage?.recordQ1?.volumeOfMobileData?.toDouble()
        val q2: Double? = mobileDataUsage?.recordQ2?.volumeOfMobileData?.toDouble()
        val q3: Double? = mobileDataUsage?.recordQ3?.volumeOfMobileData?.toDouble()
        val q4: Double? = mobileDataUsage?.recordQ4?.volumeOfMobileData?.toDouble()

        if ((q1 != null && q2 != null)
            && (q1 > q2)
        ) {
            return true
        }

        if ((q2 != null && q3 != null)
            && (q2 > q3)
        ) {
            return true
        }

        if ((q3 != null && q4 != null)
            && (q3 > q4)
        ) {
            return true
        }

        return false
    }

}