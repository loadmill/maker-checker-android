package com.loadmill.makerchecker.data.remote

import com.loadmill.makerchecker.data.*
import retrofit2.http.*

interface ApiService {

    @POST("api/login")
    suspend fun login(@Body body: LoginRequest): LoginResponse

    @GET("api/transfer/my")
    suspend fun myTransfers(@Header("token") token: String): List<Transfer>

    @POST("api/transfer/initiate")
    suspend fun initiate(
        @Header("token") token: String,
        @Body body: InitiateRequest
    ): Transfer

    @GET("api/transfer/pending")
    suspend fun pending(@Header("token") token: String): List<Transfer>

    @POST("api/transfer/approve")
    suspend fun approve(
        @Header("token") token: String,
        @Body body: TxnIdRequest
    ): Transfer

    @POST("api/transfer/reject")
    suspend fun reject(
        @Header("token") token: String,
        @Body body: TxnIdRequest
    ): Transfer
}
