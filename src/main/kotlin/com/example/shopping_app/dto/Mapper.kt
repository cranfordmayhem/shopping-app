package com.example.shopping_app.dto

import com.example.shopping_app.entity.*
import com.example.shopping_app.entity.enums.Role

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