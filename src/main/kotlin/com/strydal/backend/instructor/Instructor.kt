package com.strydal.backend.instructor

import com.strydal.backend.base.Entity
import com.strydal.backend.base.EntityWithID
import com.strydal.backend.base.ID

data class Instructor(
    val firstName: String,
    val lastName: String,
    val biography: String,
    val avatar: String
) : Entity

data class InstructorWithID(
    override val entity: Instructor,
    override val id: ID
) : EntityWithID