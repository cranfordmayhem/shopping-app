package com.example.shopping_app.repository

import com.example.shopping_app.entity.Cart
import com.example.shopping_app.entity.UserAccount
import com.example.shopping_app.entity.embedded.CartId
import org.springframework.data.domain.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CartRepository: JpaRepository<Cart, CartId> {
    fun findByUser(user: UserAccount): List<Cart>
    fun findByUserIdAndProductId(userId: Long, productId: Long): Cart?
}