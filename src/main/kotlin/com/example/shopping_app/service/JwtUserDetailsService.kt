package com.example.shopping_app.service

import com.example.shopping_app.exception.UserNotFoundException
import com.example.shopping_app.repository.UserAccountRepository
import org.springframework.security.core.userdetails.*
import org.springframework.stereotype.Service

@Service
class JwtUserDetailsService(
    private val userAccountRepo: UserAccountRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails =
        userAccountRepo.findByEmail(username)?.let { user ->
            User.builder()
                .username(user.email)
                .password(user.password)
                .roles(user.role.name) // ROLE_ prefix automatically added
                .build()
        } ?: throw UserNotFoundException(username)
}