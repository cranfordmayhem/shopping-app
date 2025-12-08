package com.example.shopping_app.service

import com.example.shopping_app.dto.*
import com.example.shopping_app.exception.*
import com.example.shopping_app.repository.UserAddressRepository
import com.example.shopping_app.utils.AuthEmailUtil
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.data.domain.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserAddressService(
    private val addressRepo: UserAddressRepository,
    private val authEmail: AuthEmailUtil
) {

    private val logger = LoggerFactory.getLogger(UserAddressService::class.java)

    fun create(addressRequest: UserAddressRequest, userEmail: String): UserAddressResponse {
        logger.debug("Creating address")
        val user = authEmail.checkUser(userEmail)
        addressRepo.clearDefaultForUser(user.id)
        return addressRepo.save(addressRequest.toEntity(user)).toResponse()
            .also{ logger.info("Address created successfully") }
    }

    fun retrieveByUser(userEmail: String, pageable: Pageable): Page<UserAddressResponse> {
        logger.debug("Retrieving all user address")
        val user = authEmail.checkUser(userEmail)
        return addressRepo.findByUserId(user.id, pageable).map {
            it.toResponse()
        }.also { logger.info("User addresses retrieved successfully") }
    }

    fun retrieveById(id: Long, userEmail: String): UserAddressResponse? =
        addressRepo.findByIdOrNull(id).apply {
            logger.debug("Retrieving address with ID $id")
            authEmail.checkAndVerifyUser(
                this!!.user.email, userEmail,
                "retrieve", "Address"
                )
        }?.toResponse().also { logger.info("Address retrieved successfully") }
            ?: throw IdNotFoundException(id, "Address")

    fun update(id: Long, addUpdateReq: UserAddressRequest, userEmail: String): UserAddressResponse =
        addressRepo.findByIdOrNull(id)?.apply {
            logger.debug("Updating address with ID $id")
            val user = authEmail.checkAndVerifyUser(
                this.user.email, userEmail,
                "update", "Address"
            )
        }?.let { existing ->
            val newData = addUpdateReq.toEntity(existing.user)
            val updated = existing.copy(
                city = newData.city,
                state = newData.state,
                zipCode = newData.zipCode,
                country = newData.country,
                contactNumber = newData.contactNumber
            )
            addressRepo.save(updated).toResponse()
                .also { logger.info("Address updated successfully") }
        } ?: throw IdNotFoundException(id, "Address")

    @Transactional
    fun setDefault(id: Long, userEmail: String): UserAddressResponse {
        return addressRepo.findByIdOrNull(id)?.apply {
            logger.debug("Updating address to default")
            val user = authEmail.checkAndVerifyUser(
                this.user.email, userEmail,
                "update", "Address"
            )

            addressRepo.clearDefaultForUser(user.id)

        }?.let { existing ->
            existing.isDefault = true
            addressRepo.save(existing).toResponse()
                .also { logger.info("Address set to default") }
        } ?: throw IdNotFoundException(id, "Address")
    }

    fun delete(id: Long, userEmail: String) =
        addressRepo.findByIdOrNull(id)?.apply {
            val user = authEmail.checkAndVerifyUser(
                this.user.email, userEmail,
                "delete", "Address"
            )
        }?.let { addressRepo.deleteById(id).also { logger.info("Address deleted successfully") } }
            ?: throw IdNotFoundException(id, "Address")
}