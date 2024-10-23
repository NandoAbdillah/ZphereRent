package com.jesando.zphererent.config

import com.jesando.zphererent.model.ForgotPasswordResponse
import com.jesando.zphererent.model.LoginResponse
import com.jesando.zphererent.model.RegisterResponse
import com.jesando.zphererent.model.ResetPasswordResponse
import com.jesando.zphererent.model.VerifyTokenResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiServices {

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
}