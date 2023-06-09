package com.todo.app.data.model

import com.google.gson.annotations.SerializedName

data class TasksResModel(
    @SerializedName("message") val message: String,
    @SerializedName("total") val total: Int,
    @SerializedName("tasks") val tasks: List<Tasks>
)
