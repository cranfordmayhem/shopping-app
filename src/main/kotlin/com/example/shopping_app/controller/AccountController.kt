package com.example.shopping_app.controller

import com.example.shopping_app.dto.*
import com.example.shopping_app.service.UserAccountService
import jakarta.validation.*
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@Validated
@RequestMapping("/account")
class AccountController(
    private val accountService: UserAccountService
) {

    @GetMapping("/{id}")
    fun getAccount(@PathVariable id: Long) =
        accountService.retrieveById(id)

    @PutMapping("/{id}")
    fun updateAccount(@PathVariable id: Long, @Valid @RequestBody accUpdate: AccountUpdateRequest) =
        accountService.update(id, accUpdate)

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun deleteAccount(@PathVariable id: Long) =
        accountService.delete(id)

}