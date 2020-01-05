package com.winsontan520.mobiledatausage.data.model


import com.google.gson.annotations.SerializedName

data class Links(
    @SerializedName("next")
    val next: String,
    @SerializedName("start")
    val start: String
)