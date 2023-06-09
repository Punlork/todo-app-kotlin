package com.todo.app.data.api

import com.todo.app.data.model.LoginReqModel
import com.todo.app.data.model.LoginResModel
import com.todo.app.data.model.LogoutResModel
import com.todo.app.data.model.TaskCreateReqModel
import com.todo.app.data.model.TaskCreateResModel
import com.todo.app.data.model.TaskUpdateReqModel
import com.todo.app.data.model.TaskUpdateResModel
import com.todo.app.data.model.TasksResModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("users/logout")
    suspend fun logout(): Response<LogoutResModel>

    @GET("tasks")
    suspend fun getTasks(@Query("date") date: String): Response<TasksResModel>

    @PATCH("tasks/{id}")
    suspend fun updateTask(
        @Path("id") taskId: String, @Body body: TaskUpdateReqModel,
    ): Response<TaskUpdateResModel>

    @POST("tasks")
    suspend fun createTask(
        @Body body: TaskCreateReqModel,
    ): Response<TaskCreateResModel>

    @POST("users")
    suspend fun signup(@Body user: LoginReqModel): Response<LoginResModel>

    @POST("users/login")
    suspend fun login(@Body user: LoginReqModel): Response<LoginResModel>
}