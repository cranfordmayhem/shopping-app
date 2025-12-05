package com.example.shopping_app.dto

data class RefreshTokenRequest(
    val token: String
)

data class TokenResponse(
    val accessToken: String
)