package com.example.shopping_app.service

import com.example.shopping_app.dto.*
import com.example.shopping_app.entity.enums.Role
import com.example.shopping_app.exception.IdNotFoundException
import com.example.shopping_app.repository.UserAccountRepository
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserAccountService(
    private val accountRepo: UserAccountRepository,
    private val passwordEncoder: PasswordEncoder
) {
    private val logger = LoggerFactory.getLogger(UserAccountService::class.java)

    fun create(accountReq: UserAccountRequest): UserAccountResponse? {
        logger.debug("Creating user")
        if(accountRepo.existsByEmail(accountReq.email))
            throw DataIntegrityViolationException("An account with this email already exists.")

        val hashedPassword = passwordEncoder.encode(accountReq.password)

        val newUser = UserAccountRequest(
            email = accountReq.email,
            password = hashedPassword
        )

        return accountRepo.save(newUser.toEntity(Role.USER)).toResponse()
            .also { logger.info("User ${it.id} created successfully") }
    }

    fun retrieveById(id: Long): UserAccountResponse {
        logger.debug("Retrieving user with $id")
        return accountRepo.findByIdOrNull(id)?.toResponse()
            .also { logger.info("Account successfully retrieved ") }
            ?: throw IdNotFoundException(id, "Account")
    }

    fun update(id: Long, accUpdateReq: AccountUpdateRequest): UserAccountResponse {
        logger.debug("updating user with $id")
        return accountRepo.findByIdOrNull(id)?.let {
            val newData = accUpdateReq.toEntity()
            if(!accountRepo.existsByEmailAndIdNot(newData.email, newData.id))
                throw DataIntegrityViolationException("An account with this email already exists")
            val hashedPass = passwordEncoder.encode(newData.password)

            val updated = it.copy(
                email = newData.email,
                password = hashedPass
            )
            accountRepo.save(updated).toResponse()
                .also { logger.info("Account updated successfully") }
        } ?: throw IdNotFoundException(id, "Account")
    }

    fun delete(id: Long) {
        logger.debug("Deleting account with $id")
        return accountRepo.findByIdOrNull(id)?.let {
            accountRepo.deleteById(id).also { logger.info("Account deleted successfully") }
        } ?: throw IdNotFoundException(id, "Account")
    }
}