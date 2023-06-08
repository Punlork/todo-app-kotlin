package com.todo.app.data.api

import com.todo.app.data.model.LoginReqModel
import com.todo.app.data.model.LoginResModel
import com.todo.app.data.model.LogoutResModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("users/logout")
    suspend fun logout(): Response<LogoutResModel>

    @POST("users")
    suspend fun signup(@Body user: LoginReqModel): Response<LoginResModel>

    @POST("users/login")
    suspend fun login(@Body user: LoginReqModel): Response<LoginResModel>
}