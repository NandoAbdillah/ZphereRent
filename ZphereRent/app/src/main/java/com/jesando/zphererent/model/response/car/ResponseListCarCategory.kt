package com.jesando.zphererent.model.response.car

import com.google.gson.annotations.SerializedName

data class ResponseListCarCategory(
    @field:SerializedName("data")
    val data: List<DataCarCategory?>? = null,

    @field: SerializedName("success")
    val success: Boolean? = null,

    @field: SerializedName("message")
    val message: String? = null
)

data class DataCarCategory(
    @field: SerializedName("id")
    val id: Int? = null,

    @field: SerializedName("name")
    val name: String? = null
)