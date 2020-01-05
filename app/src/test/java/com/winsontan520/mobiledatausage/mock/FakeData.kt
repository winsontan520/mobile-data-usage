package com.winsontan520.mobiledatausage.mock

import com.winsontan520.mobiledatausage.data.model.Field
import com.winsontan520.mobiledatausage.data.model.Links
import com.winsontan520.mobiledatausage.data.model.Record
import com.winsontan520.mobiledatausage.data.model.Result

object FakeData {
    fun createFakeResult(count: Int): Result {
        return Result(
            fields = getFields(),
            links = Links(next = "next", start = "start"),
            records = createFakeRecord(count),
            resourceId = count.toString(),
            total = count,
            limit = count
        )
    }

    private fun getFields(): List<Field> {
        return listOf(
            Field(id = "int4", type = "_id"),
            Field(id = "text", type = "text"),
            Field(id = "numeric", type = "volume_of_mobile_data")
        )
    }

    fun createFakeRecord(count: Int): List<Record> {
        return (0 until count).map {
            createFakeRecord(it.toString())
        }
    }

    private fun createFakeRecord(id: String): Record {
        return Record(id = id.toInt(), quarter = "quarter_$id", volumeOfMobileData = "volume_$id")
    }

}