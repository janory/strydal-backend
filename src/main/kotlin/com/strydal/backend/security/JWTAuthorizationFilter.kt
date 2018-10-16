package com.strydal.backend.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.strydal.backend.security.SecurityConstants.BEARER_TYPE
import com.strydal.backend.security.SecurityConstants.CLAIM_ROLE
import com.strydal.backend.security.SecurityConstants.CLAIM_USER_ID
import com.strydal.backend.security.SecurityConstants.ROLE_PREFIX
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

internal class JWTAuthorizationFilter(
    private val jwtSecretKey: ByteArray,
    authManager: AuthenticationManager
) : BasicAuthenticationFilter(authManager) {

    override fun doFilterInternal(
        req: HttpServletRequest,
        res: HttpServletResponse,
        chain: FilterChain
    ) {
        val authHeader = req.getHeader(HttpHeaders.AUTHORIZATION)

        if (authHeader == null || !authHeader.startsWith(BEARER_TYPE)) {
            chain.doFilter(req, res)
            return
        }

        val authentication = getAuthentication(authHeader)

        SecurityContextHolder.getContext().authentication = authentication
        chain.doFilter(req, res)
    }

    private fun getAuthentication(authHeader: String): UsernamePasswordAuthenticationToken {
        val token = JWT.require(Algorithm.HMAC512(jwtSecretKey))
            .build()
            .verify(authHeader.replace(BEARER_TYPE, ""))
        val emailAddress = token.subject
        val role = token.getClaim(CLAIM_ROLE).asString()
//        val userId = token.getClaim(CLAIM_USER_ID).asLong()
        val userToken =  UsernamePasswordAuthenticationToken(
            emailAddress, null, setOf(SimpleGrantedAuthority("$ROLE_PREFIX$role"))
        )
//        userToken.details = userId
        return userToken
    }
}