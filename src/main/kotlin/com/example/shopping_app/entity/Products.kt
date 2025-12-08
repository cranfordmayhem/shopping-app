package com.example.shopping_app.entity

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name="products")
data class Products(
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable=false, length=255)
    var name: String,

    @Column(name="image_url", nullable=false, length=255)
    var imageUrl: String,

    @Column(nullable=false)
    var description: String,

    @Column(nullable=false, precision=10, scale=2)
    var price: BigDecimal,

    @Column(nullable=false)
    var stock: Int
) : Auditor()