package com.strydal.backend.user

import com.strydal.backend.base.PGEnum
import org.jetbrains.exposed.dao.LongIdTable

object UsersTable : LongIdTable("users") {
    val firstName = varchar("first_name", 255)
    val lastName = varchar("last_name", 255)
    val email = varchar("email", 255).uniqueIndex()
    val password = varchar("password", 255)
    val birthday = datetime("birthday")
    val role = customEnumeration(
        "role",
        "UserRoleEnum",
        { value -> Role.valueOf(value as String) },
        { it -> PGEnum("UserRoleEnum", it) })
}

enum class Role { USER, ADMIN }