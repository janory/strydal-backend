package com.strydal.backend.security

internal object SecurityConstants {
    const val EXPIRATION_TIME: Long = 864000000 // 10 days
    const val BEARER_TYPE = "Bearer "
    const val SIGN_UP_URL = "/users/"
    const val CLAIM_ROLE = "role"
    const val CLAIM_USER_ID = "userId"
    const val ROLE_PREFIX = "ROLE_"
}