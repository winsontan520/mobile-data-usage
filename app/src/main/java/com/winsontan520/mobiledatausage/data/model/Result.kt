package com.winsontan520.mobiledatausage.data.model


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("fields")
    val fields: List<Field>,
    @SerializedName("_links")
    val links: Links,
    @SerializedName("records")
    val records: List<Record>,
    @SerializedName("resource_id")
    val resourceId: String,
    @SerializedName("total")
    val total: Int,
    @SerializedName("limit")
    val limit: Int
)