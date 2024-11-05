package com.jesando.zphererent.model.response.car

import com.google.gson.annotations.SerializedName

data class ResponseAddNewCarFeature (
    @SerializedName("car-feature")
    val carCategory: DataCarFeature? = null,
    @SerializedName("error")
    val error: Boolean? = null,
    @SerializedName("success")
    val success: Int? = null,
    @SerializedName("message")
    val message: String? = null
)