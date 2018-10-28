package com.strydal.backend.security

internal object SecurityConstants {
    const val EXPIRATION_TIME: Long = 864000000 // 10 days
    const val BEARER_TYPE = "Bearer "
    const val CLAIM_ROLE = "role"
    const val CLAIM_PERMISSIONS = "permissions"
    const val CLAIM_USER_PERMISSIONS = "user"
}