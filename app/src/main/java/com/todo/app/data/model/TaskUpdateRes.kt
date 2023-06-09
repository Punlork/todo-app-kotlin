package com.todo.app.data.model

import com.google.gson.annotations.SerializedName

data class TaskUpdateResModel(
    @SerializedName("message") val message: String
)
