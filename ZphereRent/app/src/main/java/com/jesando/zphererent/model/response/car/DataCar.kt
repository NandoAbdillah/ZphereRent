package com.jesando.zphererent.model.response.car

import com.google.gson.annotations.SerializedName

data class DataCar(
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("brand")
    val brand: String? = null,

    @field:SerializedName("model")
    val model: String? = null,

    @field:SerializedName("year")
    val year: String? = null,

    @field:SerializedName("color")
    val color: String? = null,

    @field:SerializedName("seats")
    val seats: String? = null,

    @field:SerializedName("category_id")
    val categoryId: String? = null,

    @field:SerializedName("license_plate")
    val licensePlate: String? = null,

    @field:SerializedName("price_per_day")
    val pricePerDay: String? = null,

    @field:SerializedName("available")
    val available: Int? = null,

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("fuel_type")
    val fuelType: String? = null,

    @field:SerializedName("transmission")
    val transmission: String? = null,

    @field:SerializedName("mileage")
    val mileage: String? = null,

    @field:SerializedName("last_service_date")
    val lastServiceDate: String? = null,

    @field:SerializedName("condition_notes")
    val conditionNotes: String? = null,

    @field:SerializedName("vin")
    val vin: String? = null,

    // Ubah tipe menjadi Int? untuk sesuai dengan JSON
    @field:SerializedName("in_maintenance")
    val inMaintenance: Int? = null,

    // Ubah tipe menjadi Int? untuk sesuai dengan JSON
    @field:SerializedName("insured")
    val insured: Int? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null
)
