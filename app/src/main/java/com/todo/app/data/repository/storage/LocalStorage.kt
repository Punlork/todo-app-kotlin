package com.todo.app.data.repository.storage


class LocalStorage {
    private var _token: String = ""

    fun saveToken(token: String) {
        _token = token
    }


}