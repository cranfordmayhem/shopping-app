package com.example.shopping_app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity

@SpringBootApplication
@EnableScheduling
@EnableMethodSecurity
@EnableJpaAuditing(auditorAwareRef = "springAuditorAware")
class AkcladanoApplication

fun main(args: Array<String>) {
	runApplication<AkcladanoApplication>(*args)
}
