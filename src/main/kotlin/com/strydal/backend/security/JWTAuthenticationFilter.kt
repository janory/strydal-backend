package com.strydal.backend.security


import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm.HMAC512
import com.fasterxml.jackson.databind.ObjectMapper
import com.strydal.backend.security.SecurityConstants.BEARER_TYPE
import com.strydal.backend.security.SecurityConstants.CLAIM_PERMISSIONS
import com.strydal.backend.security.SecurityConstants.CLAIM_ROLE
import com.strydal.backend.security.SecurityConstants.EXPIRATION_TIME
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

internal class JWTAuthenticationFilter(
    private val jwtSecretKey: ByteArray,
    private val objectMapper: ObjectMapper,
    private val authManager: AuthenticationManager
) : UsernamePasswordAuthenticationFilter() {

    private data class LoginUser(
        val email: String,
        val password: String
    )

    override fun attemptAuthentication(
        req: HttpServletRequest,
        res: HttpServletResponse
    ): Authentication {
        val credentials = objectMapper.readValue(req.inputStream, LoginUser::class.java)
        return authManager.authenticate(
            UsernamePasswordAuthenticationToken(
                credentials.email,
                credentials.password,
                ArrayList<GrantedAuthority>()
            )
        )
    }

    override fun successfulAuthentication(
        req: HttpServletRequest,
        res: HttpServletResponse,
        chain: FilterChain,
        auth: Authentication
    ) {
        val roleAndPermissions = auth.getRoleAndPermissionsMap()

        val signedToken = JWT.create()
            .withSubject((auth.principal as User).username)
            .withArrayClaim(
                CLAIM_PERMISSIONS,
                roleAndPermissions.getOrDefault(CLAIM_PERMISSIONS, emptySet()).toTypedArray()
            )
            .withClaim(CLAIM_ROLE, roleAndPermissions.getOrDefault(CLAIM_ROLE, emptySet()).first())
            .withExpiresAt(Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .sign(HMAC512(jwtSecretKey))

        res.addHeader(HttpHeaders.AUTHORIZATION, BEARER_TYPE + signedToken)
    }
}