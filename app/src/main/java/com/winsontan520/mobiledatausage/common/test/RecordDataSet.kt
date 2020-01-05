package com.winsontan520.mobiledatausage.common.test

import com.winsontan520.mobiledatausage.data.model.MobileDataUsage
import com.winsontan520.mobiledatausage.data.model.Record

object RecordDataSet {

    val FAKE_RECORDS = listOf(
        Record(id = 1, quarter = "2004-Q3", volumeOfMobileData = "0.000384"),
        Record(id = 2, quarter = "2004-Q4", volumeOfMobileData = "0.000543"),
        Record(id = 3, quarter = "2005-Q1", volumeOfMobileData = "0.00062"),
        Record(id = 4, quarter = "2005-Q2", volumeOfMobileData = "0.000634"),
        Record(id = 5, quarter = "2005-Q3", volumeOfMobileData = "0.000718")
    )

    val FAKE_RECORDS_WITH_DECLINE = listOf(
        Record(id = 1, quarter = "2004-Q3", volumeOfMobileData = "0.000384"),
        Record(id = 2, quarter = "2004-Q4", volumeOfMobileData = "0.000543"),
        Record(id = 3, quarter = "2005-Q1", volumeOfMobileData = "0.00062"),
        Record(id = 4, quarter = "2005-Q2", volumeOfMobileData = "0.000634"),
        Record(id = 5, quarter = "2005-Q3", volumeOfMobileData = "0.000300")
    )

    val FAKE_MOBILE_DATA_USAGES = listOf(
        MobileDataUsage(
            year = 2004,
            volumeOfMobileDataInYear = 1.000010,
            recordQ1 = Record(id = 1, quarter = "2004-Q1", volumeOfMobileData = "1.000001"),
            recordQ2 = Record(id = 2, quarter = "2004-Q2", volumeOfMobileData = "0.000002"),
            recordQ3 = Record(id = 3, quarter = "2004-Q3", volumeOfMobileData = "0.000003"),
            recordQ4 = Record(id = 4, quarter = "2004-Q4", volumeOfMobileData = "0.000004"),
            isDecline = true
        ),
        MobileDataUsage(
            year = 2005,
            volumeOfMobileDataInYear = 10.000010,
            recordQ1 = Record(id = 5, quarter = "2005-Q1", volumeOfMobileData = "1.000001"),
            recordQ2 = Record(id = 6, quarter = "2005-Q2", volumeOfMobileData = "2.000002"),
            recordQ3 = Record(id = 7, quarter = "2005-Q3", volumeOfMobileData = "3.000003"),
            recordQ4 = Record(id = 8, quarter = "2005-Q4", volumeOfMobileData = "4.000004"),
            isDecline = false
        ),
        MobileDataUsage(
            year = 2006,
            volumeOfMobileDataInYear = 10.000000,
            recordQ1 = Record(id = 9, quarter = "2006-Q1", volumeOfMobileData = "1.000000"),
            recordQ2 = Record(id = 10, quarter = "2006-Q2", volumeOfMobileData = "2.000000"),
            recordQ3 = Record(id = 11, quarter = "2006-Q3", volumeOfMobileData = "3.000000"),
            recordQ4 = Record(id = 12, quarter = "2006-Q4", volumeOfMobileData = "4.000000"),
            isDecline = false
        )
    )

    val FAKE_MOBILE_DATA_USAGES_OLD = listOf(
        MobileDataUsage(
            year = 2004,
            volumeOfMobileDataInYear = 1.000010,
            recordQ1 = Record(id = 1, quarter = "2004-Q1", volumeOfMobileData = "1.000001"),
            recordQ2 = Record(id = 2, quarter = "2004-Q2", volumeOfMobileData = "0.000002"),
            recordQ3 = Record(id = 3, quarter = "2004-Q3", volumeOfMobileData = "0.000003"),
            recordQ4 = Record(id = 4, quarter = "2004-Q4", volumeOfMobileData = "0.000004"),
            isDecline = true
        ),
        MobileDataUsage(
            year = 2005,
            volumeOfMobileDataInYear = 10.000010,
            recordQ1 = Record(id = 5, quarter = "2005-Q1", volumeOfMobileData = "1.000001"),
            recordQ2 = Record(id = 6, quarter = "2005-Q2", volumeOfMobileData = "2.000002"),
            recordQ3 = Record(id = 7, quarter = "2005-Q3", volumeOfMobileData = "3.000003"),
            recordQ4 = Record(id = 8, quarter = "2005-Q4", volumeOfMobileData = "4.000004"),
            isDecline = false
        )
    )
}