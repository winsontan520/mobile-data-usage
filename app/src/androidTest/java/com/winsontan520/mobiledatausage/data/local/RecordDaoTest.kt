package com.winsontan520.mobiledatausage.data.local

import com.winsontan520.mobiledatausage.common.test.RecordDataSet.FAKE_RECORDS
import com.winsontan520.mobiledatausage.data.model.Record
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class RecordDaoTest : BaseTest() {
    override fun setUp() {
        super.setUp()
        fillDatabase()
    }

    @Test
    fun getAllRecordsFromDb() = runBlocking {
        val records = database.recordDao().getAllRecords()
        Assert.assertEquals(5, records.size)
        compareTwoRecords(FAKE_RECORDS.first(), records.first())
    }

    private fun compareTwoRecords(record: Record, recordToTest: Record) {
        Assert.assertEquals(record.id, recordToTest.id)
        Assert.assertEquals(record.quarter, recordToTest.quarter)
        Assert.assertEquals(record.volumeOfMobileData, recordToTest.volumeOfMobileData)
    }

    private fun fillDatabase() {
        runBlocking {
            database.recordDao().save(FAKE_RECORDS)
        }
    }
}