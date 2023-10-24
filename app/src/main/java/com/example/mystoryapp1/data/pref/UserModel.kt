package com.example.mystoryapp1.data.pref

data class UserModel (
    var token: String,
    var userId: String,
    var name: String,
    var isLogin: Boolean = false
)