package com.example.shopping_app.service

import com.example.shopping_app.dto.*
import com.example.shopping_app.entity.enums.Role
import com.example.shopping_app.exception.IdNotFoundException
import com.example.shopping_app.repository.UserAccountRepository
import com.example.shopping_app.utils.AuthEmailUtil
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserAccountService(
    private val accountRepo: UserAccountRepository,
    private val authEmail: AuthEmailUtil,
    private val authService: AuthenticationService,
    private val passwordEncoder: PasswordEncoder
) {
    private val logger = LoggerFactory.getLogger(UserAccountService::class.java)

    fun create(accountReq: UserAccountRequest): UserAccountResponse? {
        logger.debug("Creating user")
        if(accountRepo.existsByEmail(accountReq.email))
            throw DataIntegrityViolationException("An account with this email already exists.")

        val hashedPass = passwordEncoder.encode(accountReq.password)

        val newUser = UserAccountRequest(
            email = accountReq.email,
            password = hashedPass
        )

        return accountRepo.save(newUser.toEntity(Role.USER)).toResponse()
            .also { logger.info("User ${it.id} created successfully") }
    }

    fun retrieveById(id: Long, userEmail: String): UserAccountResponse =
        accountRepo.findByIdOrNull(id)?.apply {
            logger.debug("Retrieving account with $id")
            val user = authEmail.checkUser(userEmail)
            authEmail.verifyUser(
                id, this.email,
                user, "retrieve", "User"
            )
        }?.toResponse().also { logger.info("Account retrieved successfully") }
            ?: throw IdNotFoundException(id, "Account")

    fun update(id: Long, accUpdateReq: AccountUpdateRequest, userEmail: String, response: HttpServletResponse): UserAccountResponse =
        accountRepo.findByIdOrNull(id)?.apply {
            logger.debug("Updating account with $id")
            val user = authEmail.checkUser(userEmail)
            authEmail.verifyUser(
                id, this.email,
                user, "update", "Account"
            )
        }?.let { existing ->
            val newData = accUpdateReq.toEntity(existing)
            val hashedPass = passwordEncoder.encode(newData.password)

            val logout = if(newData.email != existing.email) true else false

            val updated = existing.copy(
                email = newData.email,
                password = hashedPass,
                role = newData.role
            )
            accountRepo.save(updated).toResponse()
                .also {
                    logger.info("Account updated successfully")
                    if (logout) authService.logout(response)
                }
        } ?: throw IdNotFoundException(id, "Account")

    fun delete(id: Long, userEmail: String) =
        accountRepo.findByIdOrNull(id)?. apply {
            val user = authEmail.checkUser(userEmail)
            authEmail.verifyUser(id, this.email,
                user, "delete", "Account"
            )
        }?.let {
            accountRepo.deleteById(id).also { logger.info("Account deleted successfully") }
        } ?: throw IdNotFoundException(id, "Account")
}