package com.example.shopping_app.utils

import com.example.shopping_app.entity.*
import com.example.shopping_app.entity.enums.Role
import com.example.shopping_app.exception.*
import com.example.shopping_app.repository.UserAccountRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class AuthEmailUtil(
    private val accountRepo: UserAccountRepository
) {
    fun checkUser(userEmail: String): UserAccount {
        return accountRepo.findByEmail(userEmail)
            ?: throw UserNotFoundException(userEmail)
    }

    fun checkAndVerifyUser(processedEmail: String, userEmail: String, action: String, entity: String): UserAccount {
        val user = accountRepo.findByEmail(userEmail)
            ?: throw UserNotFoundException(userEmail)
        if(processedEmail != user.email && user.role != Role.ADMIN)
            throw UnauthorizedException(entity, action)
        return user
    }

    fun getUser(): String {
        val email = SecurityContextHolder.getContext().authentication.name
            ?: UnauthenticatedException()
        return email.toString()
    }
}