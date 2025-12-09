package com.example.shopping_app.service

import com.example.shopping_app.dto.*
import com.example.shopping_app.entity.*
import com.example.shopping_app.entity.enums.OrderStatus
import com.example.shopping_app.exception.*
import com.example.shopping_app.repository.*
import com.example.shopping_app.utils.AuthEmailUtil
import jakarta.transaction.Transactional
import org.slf4j.*
import org.springframework.data.domain.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.Duration
import java.time.Instant

@Service
class OrdersService(
    private val orderRepo: OrdersRepository,
    private val orderItemRepo: OrderItemRepository,
    private val cartRepo: CartRepository,
    private val authEmail: AuthEmailUtil
) {

    private val logger = LoggerFactory.getLogger(OrdersService::class.java)

    @Transactional
    fun create(userEmail: String): OrderResponse {
        logger.debug("Creating order")
        val user = authEmail.checkUser(userEmail)
        val defaultAddress = user.addresses.find { it.isDefault }
            ?: throw IdNotFoundException(user.id, "Address")
        val cartItems = cartRepo.findByUser(user)
        if(cartItems.isEmpty()) throw IllegalStateException("Cart is empty")

        cartItems.forEach { cart ->
            if(cart.quantity > cart.product.stock) {
                throw IllegalStateException(
                    "Product ${cart.product.name} only has ${cart.product.stock} left in stock"
                )
            }
        }

        cartItems.forEach { cart ->
            cart.product.stock -= cart.quantity
        }

        val totalAmount = cartItems.fold(BigDecimal.ZERO) { acc, cart ->
            acc + (cart.product.price.multiply(BigDecimal(cart.quantity)))
        }

        val order = Orders(
            user = user,
            address = defaultAddress,
            totalAmount = totalAmount,
            status = OrderStatus.PENDING
        )

        val savedOrder = orderRepo.save(order)

        val orderItems = cartItems.map { cart ->
            OrderItem(
                order = savedOrder,
                products = cart.product,
                productName = cart.product.name,
                snapshotPrice = cart.product.price,
                quantity = cart.quantity,
                price = cart.product.price.multiply(BigDecimal(cart.quantity))
            )
        }

        savedOrder.items.addAll(orderItems)
        cartRepo.deleteAll(cartItems)
        return savedOrder.toResponse(orderItems)
    }

    @PreAuthorize("hasRole('ADMIN')")
    fun retrieveAllOrders(pageable: Pageable): Page<OrderResponse> {
        return orderRepo.findAll(pageable).map { it.toResponse(it.items) }
    }

    fun retrieveOrders(userEmail: String, pageable: Pageable): Page<OrderResponse> {
        val user = authEmail.checkUser(userEmail)
         return orderRepo.findOrdersSorted(user.id, pageable)
            .map { it.toResponse(orderItemRepo.findByOrder(it)) }
    }

    fun retrieveById(orderId: Long, userEmail: String): OrderResponse {
        val user = authEmail.checkUser(userEmail)
        val order = orderRepo.findById(orderId)
            .orElseThrow { IdNotFoundException(orderId,"Order") }
        if (order.user.id != user.id) throw UnauthorizedException("retrieve", "Order")
        val items = orderItemRepo.findByOrder(order)
        return order.toResponse(items)
    }

    @PreAuthorize("hasRole('ADMIN')")
    fun updateStatus(orderId: Long, status: UpdateOrderStatusRequest): OrderResponse {
        val order = orderRepo.findById(orderId)
            .orElseThrow { IdNotFoundException(orderId, "Order") }
        order.status = status.status
        return orderRepo.save(order).toResponse(orderItemRepo.findByOrder(order))
    }

    @Transactional
    fun cancelOrder(orderId: Long, userEmail: String): OrderResponse {
        val user = authEmail.checkUser(userEmail)
        val order = orderRepo.findById(orderId)
            .orElseThrow { IdNotFoundException(orderId, "Order") }
        if (order.user.id != user.id) throw UnauthorizedException("update", "Order")
        if (order.status !in listOf(OrderStatus.PENDING, OrderStatus.PROCESSING, OrderStatus.SHIPPED))
            throw IllegalStateException("Order cannot be cancelled because it is already ${order.status}")

        val now = Instant.now()
        val createdAt = order.createdAt

        val daysElapsed = Duration.between(createdAt, now).toDays()

        if (daysElapsed > 7)
            throw IllegalStateException("Order can only be cancelled within 7 days from the order date")

        order.items.forEach { item ->
            val product = item.products
            product.stock += item.quantity
        }

        order.status = OrderStatus.CANCELLED
        val updatedOrder = orderRepo.save(order)
        return updatedOrder.toResponse(orderItemRepo.findByOrder(order))
    }
}