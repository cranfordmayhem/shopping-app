package com.example.shopping_app.dto

import com.example.shopping_app.entity.enums.Role

data class UserAccountRequest(
    val email: String,
    val password: String
)

data class UserAccountResponse(
    val id: Long,
    val email: String,
    val role: Role
)