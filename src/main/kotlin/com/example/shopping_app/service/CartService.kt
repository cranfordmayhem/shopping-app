package com.example.shopping_app.service

import com.example.shopping_app.dto.*
import com.example.shopping_app.entity.*
import com.example.shopping_app.entity.embedded.CartId
import com.example.shopping_app.exception.*
import com.example.shopping_app.repository.*
import com.example.shopping_app.utils.AuthEmailUtil
import org.slf4j.LoggerFactory
import org.springframework.data.domain.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class CartService(
    private val cartRepo: CartRepository,
    private val userRepo: UserAccountRepository,
    private val productRepo: ProductRepository,
    private val authEmail: AuthEmailUtil
) {

    private val logger = LoggerFactory.getLogger(CartService::class.java)

    fun create(request: CartRequest, userEmail: String): CartResponse {
        val user = authEmail.checkUser(userEmail)
        val product = productRepo.findByIdOrNull(request.productId)
            ?: throw IdNotFoundException(request.productId, "Product")

        val cartId = CartId(user.id, product.id)
        val cartItem = cartRepo.findById(cartId).orElse(
            Cart(id = cartId, user = user, product = product, quantity = 0)
        )

        cartItem.quantity += request.quantity
        val saved = cartRepo.save(cartItem)

        return saved.toResponse()
    }

    fun retrieveByUser(pageable: Pageable, userEmail: String): Page<CartResponse> {
        val user = authEmail.checkUser(userEmail)

        val carts: List<CartResponse> = cartRepo.findByUser(user).map { it.toResponse() }

        val start = pageable.offset.toInt()
        val end = (start + pageable.pageSize).coerceAtMost(carts.size)

        val pageContent = if (start <= end) carts.subList(start, end) else emptyList()

        return PageImpl(pageContent, pageable, carts.size.toLong())
            .also { logger.info("Carts retrieved successfully") }
    }

    fun updateCartQuantity(productId: Long, quantity: UpdateQuantityRequest, userEmail: String): CartResponse {
        val user = authEmail.checkUser(userEmail)
        val product = productRepo.findById(productId)
            .orElseThrow { IdNotFoundException(productId, "Product") }
        val cartId = CartId(user.id, product.id)

        val cart = cartRepo.findById(cartId)
            .orElseThrow { IdNotFoundException(cartId, "Cart") }
        cart.quantity = quantity.quantity

        return cartRepo.save(cart).toResponse()
    }

    fun deleteCart(productId: Long, userEmail: String) {
        val user = authEmail.checkUser(userEmail)
        val product = productRepo.findById(productId)
            .orElseThrow { IdNotFoundException(productId, "Product") }

        val cartId = CartId(user.id, product.id)
        return cartRepo.deleteById(cartId)
    }
}