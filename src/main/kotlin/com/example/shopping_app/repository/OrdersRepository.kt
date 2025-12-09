package com.example.shopping_app.repository

import com.example.shopping_app.entity.Orders
import com.example.shopping_app.entity.*
import org.springframework.data.domain.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface OrdersRepository: JpaRepository<Orders, Long> {
    fun findByUser(user: UserAccount, pageable: Pageable): Page<Orders>

    @Query(
        value = """
        SELECT * FROM orders o
        WHERE o.user_id = :userId
        ORDER BY 
            CASE 
                WHEN o.status = 'PENDING' THEN 1
                WHEN o.status = 'PROCESSING' THEN 2
                WHEN o.status = 'COMPLETED' THEN 3
                WHEN o.status = 'CANCELLED' THEN 99
                ELSE 100
            END,
            o.created_date DESC
        """,
        countQuery = """
        SELECT COUNT(*) FROM orders WHERE user_id = :userId
        """,
        nativeQuery = true
    )
    fun findOrdersSorted(userId: Long, pageable: Pageable): Page<Orders>
}