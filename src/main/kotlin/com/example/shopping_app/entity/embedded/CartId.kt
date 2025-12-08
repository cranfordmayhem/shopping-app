package com.example.shopping_app.entity.embedded

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class CartId(

    @Column(name="user_id")
    val userId: Long = 0,

    @Column(name="product_id")
    val productId: Long = 0
): Serializable