package com.example.shopping_app.dto

import com.example.shopping_app.entity.enums.OrderStatus
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class UpdateOrderStatusRequest(
    val status: OrderStatus
)

data class OrderResponse(
    val id: Long,
    val userId: Long,
    val firstName: String,
    val lastName: String,
    val address: String,
    val totalAmount: BigDecimal,
    val status: OrderStatus,
    val items: List<OrderItemResponse>
)