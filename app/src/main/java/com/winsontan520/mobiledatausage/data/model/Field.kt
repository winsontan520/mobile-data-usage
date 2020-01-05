package com.winsontan520.mobiledatausage.data.model


import com.google.gson.annotations.SerializedName

data class Field(
    @SerializedName("id")
    val id: String,
    @SerializedName("type")
    val type: String
)