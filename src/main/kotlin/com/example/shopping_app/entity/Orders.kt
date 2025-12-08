package com.example.shopping_app.entity

import com.example.shopping_app.entity.enums.OrderStatus
import jakarta.persistence.*
import java.math.BigDecimal
import kotlin.hashCode

@Entity
@Table(name="orders")
data class Orders(
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    val user: UserAccount,

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="shipping_address_id")
    val address: UserAddress,

    @Column(nullable=false, precision=10, scale=2)
    var totalAmount: BigDecimal = BigDecimal.ZERO,

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    var status: OrderStatus,

    @OneToMany(mappedBy="order", cascade=[CascadeType.ALL], fetch=FetchType.LAZY)
    val items: MutableList<OrderItem> = mutableListOf()
) : Auditor() {
    fun calculateTotalAmount(): BigDecimal =
        items.fold(BigDecimal.ZERO) { acc, item -> acc + item.price }
    override fun toString(): String = "Orders(id=$id, status=$status, totalAmount=$totalAmount)"
    override fun equals(other: Any?): Boolean = other is Orders && other.id == id
    override fun hashCode(): Int = id.hashCode()
}