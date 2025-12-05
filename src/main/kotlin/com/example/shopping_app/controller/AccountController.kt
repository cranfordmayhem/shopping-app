package com.example.shopping_app.controller

import com.example.shopping_app.dto.*
import com.example.shopping_app.service.UserAccountService
import com.example.shopping_app.utils.AuthEmailUtil
import jakarta.validation.*
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@Validated
@RequestMapping("/account")
class AccountController(
    private val accountService: UserAccountService
    private val accountService: UserAccountService,
    private val authEmail: AuthEmailUtil
) {

    @GetMapping("/{id}")
    fun getAccount(@PathVariable id: Long) =
        authEmail.getUser().apply {
            accountService.retrieveById(id, this)
        }

    @PutMapping("/{id}")
    fun updateAccount(@PathVariable id: Long, @Valid @RequestBody accUpdate: AccountUpdateRequest) =
        authEmail.getUser().apply {
            accountService.update(id, accUpdate, this)
        }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun deleteAccount(@PathVariable id: Long) =
        authEmail.getUser().apply {
            accountService.delete(id, this)
        }
}