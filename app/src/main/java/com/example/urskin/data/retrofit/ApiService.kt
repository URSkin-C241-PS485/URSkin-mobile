package com.example.urskin.data.retrofit

import com.example.urskin.data.response.LoginResponse
import com.example.urskin.data.response.ProfileResponse
import com.example.urskin.data.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("fullName") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("profile/{id}")
    suspend fun getProfile(
        @Path("id") id: String
    ): ProfileResponse
}