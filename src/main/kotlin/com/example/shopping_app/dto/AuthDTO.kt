package com.example.shopping_app.dto

import com.example.shopping_app.entity.enums.Role

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val id: Long,
    val username: String,
    val role: Role
)