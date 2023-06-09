package com.todo.app.data.model

import com.google.gson.annotations.SerializedName

data class TaskCreateResModel(
    @SerializedName("message") val message: String,
    @SerializedName("task") val task: Tasks
)
