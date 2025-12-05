package com.example.shopping_app.repository

import com.example.shopping_app.entity.UserProfile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserProfileRepository: JpaRepository<UserProfile, Long> {
    fun findByUserId(userId: Long): UserProfile?
}