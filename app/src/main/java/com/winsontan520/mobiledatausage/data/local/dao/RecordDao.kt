package com.winsontan520.mobiledatausage.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.winsontan520.mobiledatausage.data.model.Record

@Dao
abstract class RecordDao : BaseDao<Record>() {
    @Query("SELECT * from record ORDER BY quarter ASC")
    abstract suspend fun getAllRecords(): List<Record>

    suspend fun save(records: List<Record>) {
        insert(records)
    }
}