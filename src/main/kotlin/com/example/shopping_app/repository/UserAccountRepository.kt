package com.example.shopping_app.repository

import com.example.shopping_app.entity.UserAccount
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserAccountRepository: JpaRepository<UserAccount, Long> {
    fun findByEmail(email: String): UserAccount?
}