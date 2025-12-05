package com.example.shopping_app.dto

import com.example.shopping_app.entity.*
import com.example.shopping_app.entity.enums.Role
import java.time.Instant

fun UserAccountRequest.toEntity(role: Role): UserAccount = UserAccount(
    email = this.email,
    password = this.password,
    role = role
)

fun UserAccount.toResponse(): UserAccountResponse = UserAccountResponse(
    id = this.id,
    email = this.email,
    role = this.role
)


fun UserProfile.toEntity(user: UserAccount): UserProfile = UserProfile(
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