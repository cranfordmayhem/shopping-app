package com.example.shopping_app.controller

import com.example.shopping_app.dto.*
import com.example.shopping_app.service.UserAccountService
import com.example.shopping_app.utils.AuthEmailUtil
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.*
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@Validated
@RequestMapping("/account")
class AccountController(
    private val accountService: UserAccountService,
    private val authEmail: AuthEmailUtil
) {

    @GetMapping("/{id}")
    fun getAccount(@PathVariable id: Long) =
        authEmail.getUser().let {
            accountService.retrieveById(id, it)
        }

    @PutMapping("/{id}")
    fun updateAccount(
        @PathVariable id: Long,
        @Valid @RequestBody accUpdate: AccountUpdateRequest,
        response: HttpServletResponse
    ) =
        authEmail.getUser().let {
            accountService.update(id, accUpdate, it, response)
        }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun deleteAccount(@PathVariable id: Long) =
        authEmail.getUser().let {
            accountService.delete(id, it)
        }
}