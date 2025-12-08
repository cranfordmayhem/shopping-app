package com.example.shopping_app.dto

import com.example.shopping_app.entity.UserAccount
import jakarta.validation.constraints.NotBlank
import org.springframework.boot.autoconfigure.security.SecurityProperties

data class UserAddressRequest(

    @field:NotBlank(message="City is required")
    val city: String,

    @field:NotBlank(message="State is required")
    val state: String,

    @field:NotBlank(message="Zip Code is required")
    val zipCode: String,

    @field:NotBlank(message="Country is required")
    val country: String,

    @field:NotBlank(message="Contact number is required")
    val contactNumber: String
)

data class UserAddressResponse(
    val id: Long,
    val city: String,
    val state: String,
    val zipCode: String,
    val country: String,
    val contactNumber: String,
    val isDefault: Boolean,
    val userId: Long
)