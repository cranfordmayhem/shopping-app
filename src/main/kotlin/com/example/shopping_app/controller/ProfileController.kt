package com.example.shopping_app.controller

import com.example.shopping_app.dto.*
import com.example.shopping_app.service.UserProfileService
import com.example.shopping_app.utils.AuthEmailUtil
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@Validated
@RequestMapping("/profile")
class ProfileController(
    private val profileService: UserProfileService,
    private val authEmail: AuthEmailUtil
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun createProfile(@Valid @RequestBody profReq: UserProfileRequest) =
        authEmail.getUser().let {
            profileService.create(profReq, it)
        }

    @GetMapping("/me")
    fun getByLoggedInUser() =
        authEmail.getUser().let {
            profileService.retrieveByUser(it)
        }

    @GetMapping("/{id}")
    fun getByUserId(@PathVariable id: Long) =
        authEmail.getUser().let {
            profileService.retrieveById(id, it)
        }

    @PutMapping("/{id}")
    fun updateProfile(@PathVariable id: Long, profUpdateReq: UserProfileRequest) =
        authEmail.getUser().let {
            profileService.update(id, profUpdateReq, it)
        }
}