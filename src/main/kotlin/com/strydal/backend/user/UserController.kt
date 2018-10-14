package com.strydal.backend.user

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonProperty.Access
import com.strydal.backend.user.UserView.Companion.fromView
import com.strydal.backend.user.UserView.Companion.toView
import org.joda.time.DateTime
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder
import javax.servlet.http.HttpServletRequest
import javax.validation.constraints.Email

@RestController
@RequestMapping("/users", produces = [(MediaType.APPLICATION_JSON_VALUE)])
internal class UserController(private val userService: UserService) {

    @PostMapping
    fun insert(
        request: HttpServletRequest,
        @Validated @RequestBody user: UserView
    ): ResponseEntity<*> {
        return if (user.password !== user.confirmPassword) {
            ResponseEntity.badRequest().body<String?>("The passwords are different! Please check again!")
        } else {
            if (userService.findByEmail(user.email) != null) {
                ResponseEntity.badRequest().body<String?>("The Email address is already registered!")
            } else {
                val id = userService.insert(fromView(user))
                val location = UriComponentsBuilder.newInstance()
                    .scheme(request.scheme)
                    .host(request.serverName)
                    .port(request.serverPort)
                    .path("/users/$id")
                    .build()
                ResponseEntity.created(location.toUri()).build()
            }
        }
    }

    @PutMapping("/{id}")
    fun update(@PathVariable("id") id: Long, @Validated @RequestBody user: UserView) =
        userService.update(
            UserWithId(fromView(user), id)
        )

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable("id") id: Long) =
        userService.deleteById(id)

    @GetMapping
    fun findAll() =
        userService.findAll().map(::toView)

    @GetMapping("/{id}")
    fun find(@PathVariable("id") id: Long): UserView? {
        val user = userService.findById(id)
        return when (user) {
            null -> null
            else -> toView(user)
        }
    }
}

internal data class UserView(
    @JsonProperty(access = Access.READ_ONLY)
    val id: Long?,
    val firstName: String,
    val lastName: String,
    @field:Email
    val email: String,
    @JsonProperty(access = Access.WRITE_ONLY)
    val password: String,
    @JsonProperty(access = Access.WRITE_ONLY)
    val confirmPassword: String,
    val birthday: DateTime,
    @JsonProperty(access = Access.READ_ONLY)
    val role: Role?
) {
    companion object {
        fun fromView(user: UserView) =
            User(
                user.firstName,
                user.lastName,
                user.email,
                user.password,
                user.birthday,
                Role.USER
            )

        fun toView(user: UserWithId) =
            UserView(
                user.id,
                user.entity.firstName,
                user.entity.lastName,
                user.entity.email,
                "",
                "",
                user.entity.birthday,
                user.entity.role
            )
    }
}
