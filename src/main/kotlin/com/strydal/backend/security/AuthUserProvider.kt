package com.strydal.backend.security

import com.strydal.backend.user.UserService
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
internal class AuthUserProvider(
    private val userService: UserService
) : UserDetailsService {
    override fun loadUserByUsername(emailAddress: String): UserDetails {
        val user = userService.findByEmail(emailAddress)
        return when (user) {
            null -> throw UsernameNotFoundException(emailAddress)
            else -> User(
                user.entity.email,
                user.entity.password,
                setOf(SimpleGrantedAuthority(user.entity.role.toString()))
            )
        }
    }
}
