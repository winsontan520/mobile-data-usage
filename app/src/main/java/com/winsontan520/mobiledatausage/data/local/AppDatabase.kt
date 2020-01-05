package com.winsontan520.mobiledatausage.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.winsontan520.mobiledatausage.data.local.dao.RecordDao
import com.winsontan520.mobiledatausage.data.local.utils.Converters
import com.winsontan520.mobiledatausage.data.model.Record

@Database(entities = [Record::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    // DAO
    abstract fun recordDao(): RecordDao

    companion object {

        fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "app.db")
                .build()
    }
}