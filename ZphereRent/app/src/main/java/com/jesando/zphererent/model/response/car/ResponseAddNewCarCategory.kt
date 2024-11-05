package com.jesando.zphererent.model.response.car

import com.google.gson.annotations.SerializedName

data class ResponseAddNewCarCategory (
    @SerializedName("car-category")
    val carCategory: DataCarCategory? = null,
    @SerializedName("error")
    val error: Boolean? = null,
    @SerializedName("success")
    val success: Int? = null,
    @SerializedName("message")
    val message: String? = null
)