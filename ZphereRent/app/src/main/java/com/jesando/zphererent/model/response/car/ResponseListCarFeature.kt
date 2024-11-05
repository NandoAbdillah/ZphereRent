package com.jesando.zphererent.model.response.car

import com.google.gson.annotations.SerializedName

data class ResponseListCarFeature(
    @field:SerializedName("data")
    val data: List<DataCarCategory?>? = null,

    @field: SerializedName("success")
    val success: Boolean? = null,

    @field: SerializedName("message")
    val message: String? = null
)

data class DataCarFeature(
    @field: SerializedName("id")
    val id: Int? = null,

    @field: SerializedName("name")
    val name: String? = null,

    @field: SerializedName("description")
    val description: String? = null
)