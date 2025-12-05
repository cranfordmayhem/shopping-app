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

    fun verifyUser(id: Long, processedEmail: String, user: UserAccount, action: String, entity: String) {
        if(processedEmail != user.email && user.role != Role.ADMIN)
            throw UnauthorizedException(id, entity, action)
    }

    fun getUser(): String {
        val email = SecurityContextHolder.getContext().authentication.name
            ?: UnauthenticatedException()
        return email.toString()
    }
}