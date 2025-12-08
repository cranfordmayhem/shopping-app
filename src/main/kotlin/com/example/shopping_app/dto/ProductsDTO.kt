package com.example.shopping_app.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PositiveOrZero
import java.math.BigDecimal

data class ProductRequest(

    @field:NotBlank(message="Name is required")
    val name: String,

    @field:NotBlank(message="Image_URL is required")
    val imageUrl: String,

    @field:NotBlank(message="description is required")
    val description: String,

    @field:NotNull(message="price is required")
    @field:PositiveOrZero(message="Price must be greater than 0")
    val price: BigDecimal,

    @field:PositiveOrZero(message="Stock cannot be negative")
    val stock: Int? = 0
)

data class StockUpdate(
    @field:PositiveOrZero(message="Stock cannot be negative")
    val stock: Int
)

data class ProductResponse(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val description: String,
    val price: BigDecimal,
    val stock: Int
)