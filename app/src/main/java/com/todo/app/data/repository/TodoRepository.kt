package com.todo.app.data.repository

import android.content.Context
import com.todo.app.data.api.RetrofitClient
import com.todo.app.data.model.LoginReqModel
import com.todo.app.data.model.LoginResModel
import com.todo.app.data.model.LogoutResModel
import com.todo.app.data.model.TaskCreateReqModel
import com.todo.app.data.model.TaskCreateResModel
import com.todo.app.data.model.TaskUpdateReqModel
import com.todo.app.data.model.TaskUpdateResModel
import com.todo.app.data.model.TasksResModel
import com.todo.app.helper.errorConverter


class TodoRepository() {
    private val retrofitClient = RetrofitClient;

    suspend fun getTasks(
        context: Context,
        onSuccess: (TasksResModel?) -> Unit,
        onFailure: (String?) -> Unit,
        date: String,
    ): TasksResModel? {
        return try {
            val fetch = RetrofitClient.initialize(context)
            val response = fetch.getTasks(date)
            println(response)
            val body = response.body()
            val errorBody = response.errorBody()?.string()
            val errorMessage = errorConverter(errorBody)

            if (response.isSuccessful) {
                onSuccess(body)
                body
            } else {
                onFailure(errorMessage)
                null
            }

        } catch (e: Exception) {
            println(e)
            onFailure(e.message)
            null
        }
    }

    suspend fun updateTask(
        context: Context,
        onSuccess: (TaskUpdateResModel?) -> Unit,
        onFailure: (String?) -> Unit,
        id: String,
        body: TaskUpdateReqModel
    ): TaskUpdateResModel? {
        return try {
            val fetch = RetrofitClient.initialize(context)
            val response = fetch.updateTask(id, body)
            val body = response.body()
            val errorBody = response.errorBody()?.string()
            val errorMessage = errorConverter(errorBody)
            if (response.isSuccessful) {
                onSuccess(body)
                body
            } else {
                onFailure(errorMessage)
                null
            }

        } catch (e: Exception) {
            onFailure(e.message)
            null
        }
    }

    suspend fun createTask(
        context: Context,
        onSuccess: (TaskCreateResModel?) -> Unit,
        onFailure: (String?) -> Unit,
        body: TaskCreateReqModel,
    ): TaskCreateResModel? {
        return try {
            val fetch = retrofitClient.initialize(context)
            val response = fetch.createTask(body = body)
            val body = response.body()
            val errorModel = response.errorBody()?.string()
            val errorMessage = errorConverter(errorModel)
            if (response.isSuccessful) {
                onSuccess(body)
                body
            } else {
                onFailure(errorMessage)
                null
            }

        } catch (e: Exception) {
            onFailure(e.message)
            null
        }
    }
}