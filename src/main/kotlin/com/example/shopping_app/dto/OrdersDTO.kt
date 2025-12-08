package com.example.shopping_app.dto

import com.example.shopping_app.entity.enums.OrderStatus
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class OrderResponse(
    val id: Long,
    val userId: Long,
    val addressId: Long,
    val totalAmount: BigDecimal,
    val status: OrderStatus,
    val items: List<OrderItemResponse>
)