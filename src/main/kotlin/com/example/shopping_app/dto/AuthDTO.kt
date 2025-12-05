package com.example.shopping_app.dto

import com.example.shopping_app.entity.enums.Role
import jakarta.validation.constraints.*

data class LoginRequest(
    @field:NotBlank("Username is required")
    @field:Email("Username must be a valid email")
    val username: String,

    @field:NotBlank("Password is required")
    val password: String
)

data class LoginResponse(
    val id: Long,
    val username: String,
    val role: Role
)