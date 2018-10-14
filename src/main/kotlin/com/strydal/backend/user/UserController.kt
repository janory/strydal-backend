package com.strydal.backend.user

import arrow.core.Either
import arrow.core.fix
import arrow.instances.ForEither
import arrow.typeclasses.binding
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonProperty.Access
import com.strydal.backend.base.ErrorResponse
import com.strydal.backend.base.buildLocation
import com.strydal.backend.user.NewUserView.Companion.fromNewUserView
import com.strydal.backend.user.UserView.Companion.toView
import org.joda.time.DateTime
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.validation.constraints.Email

@RestController
@RequestMapping("/users", produces = [(MediaType.APPLICATION_JSON_VALUE)])
internal class UserController(private val userService: UserService, private val passwordEncoder: PasswordEncoder) {

    @PostMapping
    fun insert(
        request: HttpServletRequest,
        @Validated @RequestBody user: NewUserView
    ): ResponseEntity<*> {
        val errorOrId = ForEither<Error>() extensions {
            binding {
                val userView = isPasswordSameAsConfirmPassword(user).bind()
                val mappedUser = Either.Right(fromNewUserView(userView)).bind()
                val userId = userService.insert(
                    UserWithPassword(mappedUser, passwordEncoder.encode(user.password))
                ).bind()
                userId
            }.fix()
        }

        return when (errorOrId) {
            is Either.Left -> when (errorOrId.a) {
                is Error.PasswordsAreDifferent -> ResponseEntity.badRequest().body(ErrorResponse("The passwords are different! Please check again!"))
                is Error.EmailAlreadyRegistered -> ResponseEntity.badRequest().body(ErrorResponse("This Email address is already registered!"))
            }
            is Either.Right -> {
                ResponseEntity.created(buildLocation(errorOrId.b, request)).build<Unit>()
            }
        }
    }

//    @PutMapping("/{id}")
//    fun update(@PathVariable("id") id: Long, @Validated @RequestBody user: UserView) =
//        userService.update(
//            UserWithId(fromView(user), id)
//        )
//
//    @DeleteMapping("/{id}")
//    fun deleteById(@PathVariable("id") id: Long) =
//        userService.deleteById(id)

    @GetMapping
    fun findAll() =
        userService.findAll().map(::toView)

    @GetMapping("/{id}")
    fun find(@PathVariable("id") id: Long): ResponseEntity<UserView?> {
        val user = userService.findById(id)
        return when (user) {
            null -> ResponseEntity.notFound().build()
            else -> ResponseEntity.ok(toView(user))
        }
    }

    private companion object {
        private fun isPasswordSameAsConfirmPassword(user: NewUserView) =
            Either.cond(!user.password.isBlank() && user.password == user.confirmPassword,
                { user },
                { Error.PasswordsAreDifferent })
    }
}

internal data class NewUserView(
    val firstName: String,
    val lastName: String,
    @field:Email
    val email: String,
    val birthday: DateTime,
    @JsonProperty(access = Access.WRITE_ONLY)
    val password: String,
    @JsonProperty(access = Access.WRITE_ONLY)
    val confirmPassword: String
) {
    companion object {
        fun fromNewUserView(user: NewUserView) =
            User(
                user.firstName,
                user.lastName,
                user.email,
                user.birthday,
                Role.USER
            )
    }
}

internal data class UserView(
    @JsonProperty(access = Access.READ_ONLY)
    val id: Long,
    val firstName: String,
    val lastName: String,
    @field:Email
    @JsonProperty(access = Access.READ_ONLY)
    val email: String,
    val birthday: DateTime
) {
    companion object {
        fun fromView(user: UserView) =
            User(
                user.firstName,
                user.lastName,
                user.email,
                user.birthday,
                Role.USER
            )

        fun toView(user: UserWithId) =
            UserView(
                user.id,
                user.entity.firstName,
                user.entity.lastName,
                user.entity.email,
                user.entity.birthday
            )
    }
}
