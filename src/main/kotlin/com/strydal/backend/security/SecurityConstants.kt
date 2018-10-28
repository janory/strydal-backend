package com.strydal.backend.security

internal object SecurityConstants {
    const val EXPIRATION_TIME_IN_MILLIS: Long = 1 * 60 * 60 * 1000 // 1 hours
    const val BEARER_TYPE = "Bearer "
    const val CLAIM_ROLE = "role"
    const val CLAIM_PERMISSIONS = "permissions"
    const val CLAIM_USER_PERMISSIONS = "user"
}