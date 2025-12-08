package com.example.shopping_app.dto

import com.example.shopping_app.entity.Products
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal


data class CartRequest(
    @field:NotNull(message = "Product is required")
    val productId: Long,

    @field:NotNull(message = "Quantity is required")
    @field:Positive(message = "Quantity should be greater than 0")
    val quantity: Int
)

data class CartProductResponse(
    val id: Long,
    val imageUrl: String,
    val name: String,
    val price: BigDecimal
)

data class UpdateQuantityRequest(
    @field:NotNull(message="Quantity is required")
    @field:Positive(message="Quantity should be greater than 0")
    val quantity: Int
)

data class CartResponse(
    val userId: Long,
    val product: CartProductResponse,
    val quantity: Int
)