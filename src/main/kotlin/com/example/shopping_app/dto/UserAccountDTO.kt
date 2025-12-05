package com.example.shopping_app.dto

import com.example.shopping_app.entity.enums.Role
import jakarta.validation.constraints.*

data class UserAccountRequest(
    @field:NotBlank(message="Email is required")
    @field:Email(message="Email must be valid")
    val email: String,

    @field:NotBlank(message="Password is required")
    val password: String
)

data class AccountUpdateRequest(
    @field:Email(message="Email must be valid")
    val email: String?,
    val password: String?,
    val role: Role?
)

data class UserAccountResponse(
    val id: Long,
    val email: String,
    val role: Role
)