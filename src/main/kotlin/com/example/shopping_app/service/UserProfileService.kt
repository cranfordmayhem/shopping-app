package com.example.shopping_app.service

import com.example.shopping_app.dto.*
import com.example.shopping_app.exception.*
import com.example.shopping_app.repository.UserProfileRepository
import com.example.shopping_app.utils.AuthEmailUtil
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserProfileService(
    private val profileRepo: UserProfileRepository,
    private val authEmail: AuthEmailUtil
) {

    private val logger = LoggerFactory.getLogger(UserProfileService::class.java)

    fun create(profReq: UserProfileRequest, userEmail: String): UserProfileResponse {
        logger.debug("Creating profile")
        val user = authEmail.checkUser(userEmail)
        return profileRepo.save(profReq.toEntity(user)).toResponse()
            .also { logger.info("Profile successfully created for ${user.id}") }
    }

    fun retrieveByUser(userEmail: String): UserProfileResponse {
        logger.debug("Retrieving profile")
        val user = authEmail.checkUser(userEmail)
        return profileRepo.findByUserId(user.id)?.toResponse()
            .also { logger.info("Profile for user ${user.id} retrieved successfully") }
            ?: throw IdNotFoundException(user.id , "User")
    }

    fun retrieveById(id: Long, userEmail: String): UserProfileResponse? =
        profileRepo.findByIdOrNull(id)?.apply {
            val user = authEmail.checkUser(userEmail)
            authEmail.verifyUser(
                id, this.user.email,
                user, "retrieve", "Profile"
            )
        }?.toResponse().also { logger.info("Profile with $id retrieved successfully") }
            ?: throw IdNotFoundException(id, "Profile")

    fun update(id: Long, profUpdateReq: UserProfileRequest, userEmail: String): UserProfileResponse =
        profileRepo.findByIdOrNull(id)?.let { existing ->
            val user = authEmail.checkUser(userEmail)
            authEmail.verifyUser(id, existing.user.email, user, "update", "profile")

            val newProfData = profUpdateReq.toEntity(existing.user)
            val updated = existing.copy(
                firstName = newProfData.firstName,
                lastName = newProfData.lastName,
                age = newProfData.age
            )

            profileRepo.save(updated).toResponse()
                .also { logger.info("Profile updated successfully") }
        } ?: throw IdNotFoundException(id, "Profile")

    fun delete(id: Long, userEmail: String) =
        profileRepo.findByIdOrNull(id)?.apply {
            val user = authEmail.checkUser(userEmail)
            authEmail.verifyUser(
                id, this.user.email,
                user, "delete", "Profile"
            )
        }?.let { profileRepo.deleteById(id).also { logger.info("Profile deleted successfully") } }
            ?: throw IdNotFoundException(id, "Profile")
}