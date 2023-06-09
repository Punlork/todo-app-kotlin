package com.todo.app.data.model

import com.google.gson.annotations.SerializedName

data class TaskUpdateReqModel(
    @SerializedName("completed") val completed: Boolean
)
