package com.todo.app.helper

import com.google.gson.Gson
import com.google.gson.GsonBuilder


fun <T> classFormatter(x: T): String? {
    val gson: Gson = GsonBuilder().setPrettyPrinting().create()
    return gson.toJson(x)
}