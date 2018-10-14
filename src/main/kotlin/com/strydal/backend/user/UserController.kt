package com.strydal.backend.user

import arrow.core.Either
import arrow.core.fix
import arrow.instances.ForEither
import arrow.typeclasses.binding
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

val <T> T.exhaustive: T get() = this


@RestController
@RequestMapping("/users", produces = [(MediaType.APPLICATION_JSON_VALUE)])
internal class UserController(private val userService: UserService) {

    @PostMapping
    fun insert(
        request: HttpServletRequest,
        @Validated @RequestBody user: UserView
    ): ResponseEntity<String> {
        val errorOrId = ForEither<Error>() extensions {
            binding {
                val userView = isPasswordSameAsConfirmPassword(user).bind()
                val mappedUser = Either.Right(fromView(userView)).bind()
                val id = userService.insert(mappedUser).bind()
                id
            }.fix()
        }

        return when (errorOrId) {
            is Either.Left -> when (errorOrId.a) {
                is Error.PasswordsAreDifferent -> ResponseEntity.badRequest().body("The passwords are different! Please check again!")
                is Error.EmailAlreadyRegistered -> ResponseEntity.badRequest().body("The Email address is already registered!")
            }
            is Either.Right -> {
                val path = "/users/${errorOrId.b}"
                ResponseEntity.created(buildLocation(path, request)).build()
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

    private companion object {
        private fun buildLocation(path: String, request: HttpServletRequest) =
            UriComponentsBuilder.newInstance()
                .scheme(request.scheme)
                .host(request.serverName)
                .port(request.serverPort)
                .path(path)
                .build()
                .toUri()


        private fun isPasswordSameAsConfirmPassword(user: UserView) =
            Either.cond(user.password != user.confirmPassword,
                { user },
                { Error.PasswordsAreDifferent })
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

object Do {
    inline infix fun <reified T> exhaustive(any: T?) = any
}