package com.example.shopping_app.exception

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler {

    object ErrorUtil {

        private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

        fun errorMapper(status: Int, error: String, request: HttpServletRequest): Map<String, Any> {
            return mapOf(
                "timestamp" to LocalDateTime.now(),
                "status" to status,
                "error" to error,
                "path" to request.requestURI
            )
        }
        fun errorHandler(error: String) {

            return logger.error("ERROR: $error")
        }
    }

}