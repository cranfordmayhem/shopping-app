package com.example.shopping_app.dto

import com.example.shopping_app.entity.embedded.OrderItemId
import java.math.BigDecimal


data class OrderItemResponse(
    val id: OrderItemId,
    val orderId: Long,
    val productId: Long,
    val productName: String,
    val snapshotPrice: BigDecimal,
    val quantity: Int,
    val price: BigDecimal
)