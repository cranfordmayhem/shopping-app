package com.example.shopping_app.controller

import com.example.shopping_app.dto.*
import com.example.shopping_app.service.ProductService
import com.example.shopping_app.utils.AuthEmailUtil
import jakarta.validation.Valid
import org.springframework.data.domain.*
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@Validated
@RequestMapping("/product")
class ProductController(
    private val productService: ProductService,
    private val authEmail: AuthEmailUtil
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun addProduct(@Valid @RequestBody addRequest: ProductRequest) =
        productService.create(addRequest)

    @GetMapping
    fun getAllProducts(pageable: Pageable) =
        productService.retrieve(pageable)

    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: Long) =
        productService.retrieveById(id)

    @PutMapping("/{id}")
    fun updateProduct(@PathVariable id: Long, @Valid @RequestBody updateRequest: ProductRequest) =
        productService.update(id, updateRequest)

    @PatchMapping("/{id}")
    fun updateStock(@PathVariable id: Long, @Valid @RequestBody stock: StockUpdate) =
        productService.updateStock(id, stock)

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: Long) =
        productService.delete(id)

}