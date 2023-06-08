package com.todo.app.data.repository

import android.content.Context
import com.google.gson.Gson
import com.todo.app.data.api.RetrofitClient
import com.todo.app.data.model.ErrorResModel
import com.todo.app.data.model.LoginReqModel
import com.todo.app.data.model.LoginResModel
import com.todo.app.data.model.LogoutResModel
import com.todo.app.helper.errorConverter


class TodoRepository() {
    private val retrofitClient = RetrofitClient;
    suspend fun login(
        user: LoginReqModel, onSuccess: (LoginResModel?) -> Unit, onFailure: (String?) -> Unit
    ): LoginResModel? {
        return try {
            val response = retrofitClient.getApiService().login(user)
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
            println("Error")
            onFailure(e.message)
            null
        }
    }

    suspend fun logout(
        context: Context, onSuccess: (LogoutResModel?) -> Unit, onFailure: (String?) -> Unit
    ): LogoutResModel? {
        return try {
            val fetch = RetrofitClient.initialize(context)
            val response = fetch.logout()
            val result = response.body()
            if (response.isSuccessful) {
                onSuccess(result)
                result
            } else {
                onFailure(result?.message)
                null
            }

        } catch (e: Exception) {
            onFailure(e.message)
            null
        }
    }
}