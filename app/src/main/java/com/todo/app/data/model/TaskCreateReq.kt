package com.todo.app.data.model

import com.google.gson.annotations.SerializedName

data class TaskCreateReqModel(
    @SerializedName("description") val description: String,
    @SerializedName("date") val date: String,
)
