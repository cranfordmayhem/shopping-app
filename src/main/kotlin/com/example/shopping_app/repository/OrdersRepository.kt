package com.example.shopping_app.repository

import com.example.shopping_app.entity.Orders
import com.example.shopping_app.entity.*
import org.springframework.data.domain.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrdersRepository: JpaRepository<Orders, Long> {
    fun findByUser(user: UserAccount, pageable: Pageable): Page<Orders>
}