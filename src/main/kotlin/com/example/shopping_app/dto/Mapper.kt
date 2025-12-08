package com.example.shopping_app.dto

import com.example.shopping_app.entity.*
import com.example.shopping_app.entity.enums.Role
import java.time.Instant

fun UserAccountRequest.toEntity(role: Role): UserAccount = UserAccount(
    email = this.email,
    password = this.password,
    role = role
)

fun AccountUpdateRequest.toEntity(existing: UserAccount): UserAccount = existing.copy(
    email = this.email ?: existing.email,
    password = this.password ?: existing.password,
    role = this.role ?: existing.role
)

fun UserAccount.toResponse(): UserAccountResponse = UserAccountResponse(
    id = this.id,
    email = this.email,
    role = this.role
)


fun UserProfileRequest.toEntity(user: UserAccount): UserProfile = UserProfile(
    firstName = this.firstName,
    lastName = this.lastName,
    age = this.age,
    user = user
)

fun UserProfile.toResponse(): UserProfileResponse = UserProfileResponse(
    id = this.id,
    firstName = this.firstName,
    lastName = this.lastName,
    age = this.age,
    email = user.email
)

fun RefreshTokenRequest.toEntity(user: UserAccount, expiryDate: Instant):
        RefreshToken = RefreshToken(
            token = this.token,
            expiryDate = expiryDate,
            userAccount = user
)

fun UserAddressRequest.toEntity(user: UserAccount): UserAddress = UserAddress(
    city = this.city,
    state = this.state,
    zipCode = this.zipCode,
    country = this.country,
    contactNumber = this.contactNumber,
    isDefault = true,
    user = user
)

fun UserAddress.toResponse(): UserAddressResponse = UserAddressResponse(
    id = this.id,
    city = this.city,
    state = this.state,
    zipCode = this.zipCode,
    country = this.country,
    contactNumber = this.contactNumber,
    isDefault = this.isDefault,
    userId = this.user.id
)