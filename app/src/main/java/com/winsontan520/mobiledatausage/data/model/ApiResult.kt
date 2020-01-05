package com.winsontan520.mobiledatausage.data.model


import com.google.gson.annotations.SerializedName

data class ApiResult(
    @SerializedName("help")
    val help: String,
    @SerializedName("result")
    val result: Result,
    @SerializedName("success")
    val success: Boolean
)