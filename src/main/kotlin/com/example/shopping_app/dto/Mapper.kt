package com.example.shopping_app.dto

import com.example.shopping_app.entity.*
import com.example.shopping_app.entity.enums.Role
import java.time.Instant

fun UserAccountRequest.toEntity(role: Role): UserAccount = UserAccount(
    email = this.email,
    password = this.password,
    role = role
)

fun AccountUpdateRequest.toEntity(existing: UserAccount): UserAccount = existing.copy(
    email = this.email ?: existing.email,
    password = this.password ?: existing.password,
    role = this.role ?: existing.role
)

fun UserAccount.toResponse(): UserAccountResponse = UserAccountResponse(
    id = this.id,
    email = this.email,
    role = this.role
)


fun UserProfileRequest.toEntity(user: UserAccount): UserProfile = UserProfile(
    firstName = this.firstName,
    lastName = this.lastName,
    age = this.age,
    user = user
)

fun UserProfile.toResponse(): UserProfileResponse = UserProfileResponse(
    id = this.id,
    firstName = this.firstName,
    lastName = this.lastName,
    age = this.age,
    email = user.email
)

fun RefreshTokenRequest.toEntity(user: UserAccount, expiryDate: Instant):
        RefreshToken = RefreshToken(
            token = this.token,
            expiryDate = expiryDate,
            userAccount = user
)

fun UserAddressRequest.toEntity(user: UserAccount): UserAddress = UserAddress(
    city = this.city,
    state = this.state,
    zipCode = this.zipCode,
    country = this.country,
    contactNumber = this.contactNumber,
    isDefault = true,
    user = user
)

fun UserAddress.toResponse(): UserAddressResponse = UserAddressResponse(
    id = this.id,
    city = this.city,
    state = this.state,
    zipCode = this.zipCode,
    country = this.country,
    contactNumber = this.contactNumber,
    isDefault = this.isDefault,
    userId = this.user.id
)

fun ProductRequest.toEntity(): Products = Products(
    name = this.name,
    imageUrl = this.imageUrl,
    description = this.description,
    price = this.price,
    stock = this.stock ?: 0
)

fun Products.toResponse(): ProductResponse = ProductResponse(
    id = this.id,
    name = this.name,
    imageUrl = this.imageUrl,
    description = this.description,
    price = this.price,
    stock = this.stock
)

fun CartRequest.toEntity(user: UserAccount, product: Products): Cart = Cart(
    id = CartId(userId = user.id, productId = product.id),
    user = user,
    product = product,
    quantity = this.quantity
)

fun Cart.toResponse(): CartResponse = CartResponse(
    userId = this.user.id,
    product = CartProductResponse(
        id = this.product.id,
        imageUrl = this.product.imageUrl,
        name = this.product.name,
        price = this.product.price
    ),
    quantity = this.quantity
)
fun Orders.toResponse(items: List<OrderItem>): OrderResponse = OrderResponse(
    id = this.id,
    userId = this.user.id,
    addressId = this.address.id,
    totalAmount = this.calculateTotalAmount(),
    status = this.status,
    items = this.items.map { it.toResponse() }
)

fun OrderItem.toResponse(): OrderItemResponse = OrderItemResponse(
    id = this.id,
    orderId = this.order.id,
    productId = this.products.id,
    snapshotPrice = this.snapshotPrice,
    quantity = this.quantity,
    price = this.price
)