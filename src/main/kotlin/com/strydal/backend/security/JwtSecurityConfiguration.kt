package com.strydal.backend.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.strydal.backend.security.SecurityConstants.CLAIM_PERMISSIONS
import com.strydal.backend.security.SecurityConstants.CLAIM_ROLE
import com.strydal.backend.security.SecurityConstants.CLAIM_USER_PERMISSIONS
import com.strydal.backend.user.Role
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.util.*

@EnableWebSecurity
internal class JwtSecurityConfiguration(
    private val authUserProvider: AuthUserProvider,
    private val objectMapper: ObjectMapper,
    private val passwordEncoder: PasswordEncoder,
    @Value("\${spring.security.jwt_secret_key}")
    private val jwtSecretKeyBase64: String
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        val jwtSecretKey = Base64.getDecoder().decode(jwtSecretKeyBase64)
        http.cors().and().csrf().disable().authorizeRequests()
            .mvcMatchers(HttpMethod.POST, "/users").permitAll()
            .mvcMatchers(HttpMethod.GET, "/users").hasRole(Role.ADMIN.toString())
            .mvcMatchers(HttpMethod.GET, "/users/{userId}/**").access("@customPermissionChecker.isOwnResource(#userId)")
            .mvcMatchers(HttpMethod.PUT).hasRole(Role.ADMIN.toString())
            .mvcMatchers(HttpMethod.POST).hasRole(Role.ADMIN.toString())
            .mvcMatchers(HttpMethod.DELETE).hasRole(Role.ADMIN.toString())
            .anyRequest().authenticated()
            .and()
            .addFilter(JWTAuthenticationFilter(jwtSecretKey, objectMapper, authenticationManager()))
            .addFilter(JWTAuthorizationFilter(jwtSecretKey, authenticationManager()))
            // this disables session creation on Spring Security
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }


    public override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService<AuthUserProvider>(authUserProvider).passwordEncoder(passwordEncoder)
    }
}

@Component
internal class CustomPermissionChecker {
    fun isOwnResource(userId: String): Boolean {
        val authentication = SecurityContextHolder.getContext().authentication
        val roleAndPermissions = authentication.getRoleAndPermissionsMap()
        return roleAndPermissions.getValue(CLAIM_ROLE).first() == Role.ADMIN.toString() ||
                roleAndPermissions[CLAIM_PERMISSIONS]?.let {
                    val userPerm = it.find { it.startsWith(CLAIM_USER_PERMISSIONS) }
                    userPerm?.let {
                        it.substringAfterLast(':') == userId
                    } ?: false
                } ?: false
    }
}
