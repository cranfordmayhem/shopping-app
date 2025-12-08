package com.example.shopping_app.entity

import com.example.shopping_app.entity.embedded.OrderItemId
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name="order_item")
data class OrderItem(
    @EmbeddedId
    val id: OrderItemId = OrderItemId(),

    @ManyToOne(fetch=FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name="order_id")
    val order: Orders,

    @ManyToOne(fetch=FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name="product_id")
    val products: Products,

    @Column(nullable=false, length=255)
    var productName: String,

    @Column(name="product_snapshot_price", nullable=false, precision=10, scale=2)
    var snapshotPrice: BigDecimal,

    @Column(nullable=false)
    var quantity: Int,

    @Column(nullable=false, precision=10, scale=2)
    val price : BigDecimal
) : Auditor()