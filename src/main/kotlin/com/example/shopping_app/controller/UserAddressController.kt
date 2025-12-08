package com.example.shopping_app.controller

import com.example.shopping_app.dto.*
import com.example.shopping_app.service.UserAddressService
import com.example.shopping_app.utils.AuthEmailUtil
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@Validated
@RequestMapping("/address")
class UserAddressController(
    private val addressService: UserAddressService,
    private val authEmail: AuthEmailUtil
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun add(@Valid @RequestBody addRequest: UserAddressRequest) =
        authEmail.getUser().let {
            addressService.create(addRequest, it)
        }

    @GetMapping
    fun getAddress(pageable: Pageable) =
        authEmail.getUser().let {
            addressService.retrieveByUser(it, pageable)
        }

    @GetMapping("/{id}")
    fun getAddressById(@PathVariable id: Long) =
        authEmail.getUser().let{
            addressService.retrieveById(id, it)
        }

    @PutMapping("/{id}")
    fun updateAddress(@PathVariable id: Long, @Valid @RequestBody updateRequest: UserAddressRequest) =
        authEmail.getUser().let {
            addressService.update(id, updateRequest,it)
        }

    @PatchMapping("/{id}/setDefault")
    fun updateDefault(@PathVariable id: Long) =
        authEmail.getUser().let {
            addressService.setDefault(id, it)
        }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun deleteAddress(@PathVariable id: Long) =
        authEmail.getUser().let {
            addressService.delete(id, it)
        }
}