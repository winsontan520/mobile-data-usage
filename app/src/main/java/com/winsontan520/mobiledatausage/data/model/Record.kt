package com.winsontan520.mobiledatausage.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Record(
    @PrimaryKey
    @SerializedName("_id")
    val id: Int,
    @SerializedName("quarter")
    val quarter: String,
    @SerializedName("volume_of_mobile_data")
    val volumeOfMobileData: String
)