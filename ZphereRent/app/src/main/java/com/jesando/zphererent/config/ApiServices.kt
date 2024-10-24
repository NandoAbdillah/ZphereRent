package com.jesando.zphererent.config

import com.jesando.zphererent.model.response.auth.ForgotPasswordResponse
import com.jesando.zphererent.model.response.auth.LoginResponse
import com.jesando.zphererent.model.response.auth.RegisterResponse
import com.jesando.zphererent.model.response.auth.ResetPasswordResponse
import com.jesando.zphererent.model.response.auth.VerifyTokenResponse
import com.jesando.zphererent.model.response.car.ResponseAddNewCar
import com.jesando.zphererent.model.response.car.ResponseListCarCategory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiServices {


    // Auth Services
    // Endpoint untuk register
    @FormUrlEncoded
    @POST("register")
    fun registerUser(
        @Field("name") name : String,
        @Field("email") email : String,
        @Field("password") password : String,
        @Field("password_confirmation") passwordConfirmation : String,
    ) : Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("forgot-password")
    fun forgotPassword(
        @Field("email") email: String
    ): Call<ForgotPasswordResponse>

    @POST("logout")
    fun logoutUser(
        @Header("Authorization") token: String
    ): Call<Void>

    @FormUrlEncoded
    @POST("verify-token")
    fun verifyToken(
        @Field("email") email: String,
        @Field("token") token: String
    ): Call<VerifyTokenResponse>

    @FormUrlEncoded
    @POST("reset-password")
    fun resetPassword(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("password_confirmation") passwordConfirmation: String
    ): Call<ResetPasswordResponse>


    // Car Cateogory

    @GET("car-categories")
    fun getCarCategories(): Call<ResponseListCarCategory>

    @Multipart
    @POST("cars")
    fun postCar(
        @Part("brand") brand: RequestBody,
        @Part("model") model: RequestBody,
        @Part("year") year: RequestBody,
        @Part("color") color: RequestBody,
        @Part("seats") seats: RequestBody,
        @Part("category_id") categoryId: RequestBody,
        @Part("license_plate") licensePlate: RequestBody,
        @Part("price_per_day") pricePerDay: RequestBody,
        @Part("fuel_type") fuelType: RequestBody,
        @Part("transmission") transmission: RequestBody,
        @Part("mileage") mileage: RequestBody,
        @Part("description") description: RequestBody?,
        @Part image: MultipartBody.Part?
    ): Call<ResponseAddNewCar>
}