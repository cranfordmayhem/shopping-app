package com.example.shopping_app.controller

import com.example.shopping_app.dto.*
import com.example.shopping_app.service.CartService
import com.example.shopping_app.utils.AuthEmailUtil
import jakarta.validation.Valid
import org.springframework.data.domain.*
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@Validated
@RequestMapping("/cart")
class CartController(
    private val cartService: CartService,
    private val authEmail: AuthEmailUtil
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun addCart(@RequestBody addRequest: CartRequest) =
        authEmail.getUser().let {
            cartService.create(addRequest, it)
        }

    @GetMapping
    fun getCartByUser(pageable: Pageable) =
        authEmail.getUser().let {
            cartService.retrieveByUser(pageable, it)
        }

    @PutMapping("/product/{productId}")
    fun updateQuantity(@PathVariable productId: Long, @Valid @RequestBody quantity: UpdateQuantityRequest) =
        authEmail.getUser().let {
            cartService.updateCartQuantity(productId, quantity, it)
        }

    @DeleteMapping("/product/{productId}")
    fun removeCart(@PathVariable productId: Long) =
        authEmail.getUser().let {
            cartService.deleteCart(productId, it)
        }
}