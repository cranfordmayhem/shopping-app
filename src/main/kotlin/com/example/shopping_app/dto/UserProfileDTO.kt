package com.example.shopping_app.dto

import jakarta.validation.constraints.*

data class UserProfileRequest(
    @field:NotBlank(message="First name is required")
    val firstName: String,

    @field:NotBlank(message="Last name is required")
    val lastName: String,

    @field:Min(value=1, message="Age must be at least 1")
    val age: Int
)

data class UserProfileResponse(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val age: Int,
    val email: String
)