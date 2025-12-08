package com.example.shopping_app.entity.embedded

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class OrderItemId(

    @Column(name="order_id")
    val orderId: Long = 0,

    @Column(name="product_id")
    val productId: Long = 0,
): Serializable