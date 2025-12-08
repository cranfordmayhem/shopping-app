package com.example.shopping_app.repository

import com.example.shopping_app.entity.OrderItem
import com.example.shopping_app.entity.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderItemRepository: JpaRepository<OrderItem, Long> {
    fun findByOrder(order: Orders): List<OrderItem>
}