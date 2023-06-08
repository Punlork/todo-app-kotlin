package com.todo.app.data.api

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://pnek-todo-api.onrender.com/"
    private var token: String = ""

    private val okHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
        val originalRequest = chain.request()
        val modifiedRequest = originalRequest.newBuilder().header("Authorization", "Bearer $token")
            // Add more headers if needed
            .build()
        chain.proceed(modifiedRequest)
    }.build()

    private val retrofit: Retrofit =
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient).build()

    private var apiService: ApiService = retrofit.create(ApiService::class.java)

    private fun getToken(context: Context): String {
        val sharedPref = context.getSharedPreferences("my_preference", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", "");
        return if (token !== null) token else ""
    }

    fun initialize(context: Context): ApiService {
        this.token = getToken(context)
        // Use the context as needed
        return apiService;
    }

    fun getApiService(): ApiService {
        return apiService
    }

}
