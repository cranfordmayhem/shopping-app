package com.example.shopping_app.controller

import com.example.shopping_app.entity.enums.OrderStatus
import com.example.shopping_app.service.OrdersService
import com.example.shopping_app.utils.AuthEmailUtil
import org.springframework.data.domain.*
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/order")
class OrderController(
    private val orderService: OrdersService,
    private val authEmail: AuthEmailUtil
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun addOrder() =
        authEmail.getUser().let {
            orderService.create(it)
        }

    @GetMapping
    fun getAllOrders(pageable: Pageable) =
        authEmail.getUser().let {
            orderService.retrieveOrders(it, pageable)
        }

    @GetMapping("/{id}")
    fun getOrderById(@PathVariable id: Long) =
        authEmail.getUser().let {
            orderService.retrieveById(id, it)
        }

    @PutMapping("/{id}")
    fun updateOrderStatus(@PathVariable id: Long, status: OrderStatus) =
        authEmail.getUser().let {
            orderService.updateStatus(id, status)
        }

    @PatchMapping("/{id}/cancel")
    fun cancelOrder(@PathVariable id: Long) =
        authEmail.getUser().let {
            orderService.cancelOrder(id, it)
        }
}