package com.example.shopping_app.repository

import com.example.shopping_app.entity.UserAddress
import org.springframework.data.domain.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserAddressRepository: JpaRepository<UserAddress, Long> {
    fun findByUserId(userId: Long, pageable: Pageable): Page<UserAddress>

    @Modifying
    @Query("UPDATE UserAddress a SET a.isDefault = false WHERE a.user.id = :userId AND a.isDefault = true")
    fun clearDefaultForUser(userId: Long)
}