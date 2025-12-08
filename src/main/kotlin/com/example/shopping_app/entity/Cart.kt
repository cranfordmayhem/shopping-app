package com.example.shopping_app.entity

import com.example.shopping_app.entity.embedded.CartId
import jakarta.persistence.*

@Entity
@Table(name="cart")
data class Cart(
    @EmbeddedId
    val id: CartId = CartId(),

    @ManyToOne(fetch=FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name="user_id")
    var user: UserAccount,

    @ManyToOne(fetch=FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name="product_id")
    var product: Products,

    @Column(nullable=false)
    var quantity: Int,
) : Auditor()