package com.example.shopping_app.service

import com.example.shopping_app.dto.*
import com.example.shopping_app.exception.*
import com.example.shopping_app.repository.ProductRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service

@Service
class ProductService(
    private val productRepo: ProductRepository
) {
    private val logger = LoggerFactory.getLogger(ProductService::class.java)

    @PreAuthorize("hasRole('ADMIN')")
    fun create(productReq: ProductRequest): ProductResponse {
        logger.debug("Creating product")
        return productRepo.save(productReq.toEntity()).toResponse()
            .also { logger.info("Product created successfully") }
    }

    fun retrieve(pageable: Pageable): Page<ProductResponse> {
        logger.debug("Retrieving all products")
        return productRepo.findAll(pageable).map {
            it.toResponse()
        }.also { logger.info("Products retrieved successfully") }
    }

    fun retrieveById(id: Long): ProductResponse? {
        logger.debug("Retrieving product with ID $id")
        return productRepo.findByIdOrNull(id)?.toResponse()
            .also { logger.info("Product retrieved successfully") }
            ?: throw IdNotFoundException(id, "Product")

    }

    @PreAuthorize("hasRole('ADMIN')")
    fun update(id: Long, prodUpdateReq: ProductRequest): ProductResponse {
        logger.debug("Updating product with ID $id")
        return productRepo.findByIdOrNull(id)?.let { existing ->
            val newData = prodUpdateReq.toEntity()
            val updated = existing.copy(
                name = newData.name,
                imageUrl = newData.imageUrl,
                description = newData.description,
                price = newData.price,
                stock = newData.stock
            )

            productRepo.save(updated).toResponse()
                .also { logger.info("Product updated successfully") }
        } ?: throw IdNotFoundException(id, "Product")
    }

    @PreAuthorize("hasRole('ADMIN')")
    fun updateStock(id: Long, stock: StockUpdate): ProductResponse {
        logger.debug("Updating stocks of product $id")
        return productRepo.findByIdOrNull(id)?.let { existing ->
            existing.stock = stock.stock

            productRepo.save(existing).toResponse()
                .also { logger.info("product stocks updated successfully") }
        } ?: throw IdNotFoundException(id, "Product")
    }

    @PreAuthorize("hasRole('ADMIN')")
    fun delete(id: Long) {
        logger.debug("Deleting product with ID $id")
        productRepo.findByIdOrNull(id)?.let {
            productRepo.deleteById(id).also { logger.info("Product deleted successfully") }
        } ?: throw IdNotFoundException(id, "Product")
    }
}