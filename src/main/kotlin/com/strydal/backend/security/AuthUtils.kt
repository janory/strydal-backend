package com.strydal.backend.security

import org.springframework.security.core.Authentication

internal fun Authentication.getRoleAndPermissionsMap() =
    this.authorities.fold(emptyMap<String, Set<String>>()) { acc, eachAuthority ->
        val authorityStr = eachAuthority.authority
        val authorityWithOutPrefix = authorityStr.substringAfter('_')
        when {
            authorityStr.startsWith(SecurityConstants.CLAIM_ROLE, true) ->
                acc + (SecurityConstants.CLAIM_ROLE to setOf(authorityWithOutPrefix))
            else -> {
                val permissions = when (acc[SecurityConstants.CLAIM_PERMISSIONS]) {
                    null -> setOf(authorityWithOutPrefix)
                    else -> acc.getValue(SecurityConstants.CLAIM_PERMISSIONS) + authorityWithOutPrefix
                }
                acc + (SecurityConstants.CLAIM_PERMISSIONS to permissions)
            }
        }
    }