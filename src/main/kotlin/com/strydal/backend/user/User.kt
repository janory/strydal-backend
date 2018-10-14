package com.strydal.backend.user

import com.strydal.backend.base.Entity
import com.strydal.backend.base.EntityWithID
import com.strydal.backend.base.ID
import org.joda.time.DateTime

data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val birthday: DateTime,
    val role: Role
) : Entity

data class UserWithId(
    override val entity: User,
    override val id: ID
) : EntityWithID