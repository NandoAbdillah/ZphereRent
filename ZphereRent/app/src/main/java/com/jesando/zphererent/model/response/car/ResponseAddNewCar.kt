package com.jesando.zphererent.model.response.car

import com.google.gson.annotations.SerializedName

data class ResponseAddNewCar(
    @field:SerializedName("car")
    val car: DataCar? = null,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("success")
    val success: Int? = null,  // Ubah tipe menjadi Int sesuai contoh

    @field:SerializedName("message")
    val message: String? = null
)


