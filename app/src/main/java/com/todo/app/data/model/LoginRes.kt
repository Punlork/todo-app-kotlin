package com.todo.app.data.model

import com.google.gson.annotations.SerializedName

data class LoginResModel(
    @SerializedName("user") var user: User? = User(),
    @SerializedName("token") var token: String? = null,
)

data class User(
    @SerializedName("_id") var Id: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("tasks") var tasks: ArrayList<Tasks> = arrayListOf(),
    @SerializedName("totalTask") var totalTask: Int? = null
)

data class Tasks(

    @SerializedName("_id") var Id: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("completed") var completed: Boolean? = null,
    @SerializedName("owner") var owner: String? = null

)