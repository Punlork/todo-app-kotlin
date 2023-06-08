package com.todo.app.helper

import com.google.gson.Gson
import com.todo.app.data.model.ErrorResModel

fun errorConverter(errorBody: String?): String {
    if (errorBody !== null){
        val gson = Gson()
        val errorModel = gson.fromJson(errorBody, ErrorResModel::class.java)
        return errorModel.message
    }
    return "Something went wrong!"
}
