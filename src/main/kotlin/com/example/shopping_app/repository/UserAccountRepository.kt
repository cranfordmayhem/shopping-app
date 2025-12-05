package com.example.shopping_app.repository

import com.example.shopping_app.entity.UserAccount
import com.example.shopping_app.entity.enums.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserAccountRepository: JpaRepository<UserAccount, Long> {
    fun findByEmail(email: String): UserAccount?
    fun existsByEmail(email: String): Boolean
    fun existsByEmailAndIdNot(email: String, id: Long) : Boolean
    fun existsByRole(role: Role): Boolean
}