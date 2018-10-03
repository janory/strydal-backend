package com.strydal.backend.instructor

import org.jetbrains.exposed.dao.LongIdTable

object InstructorsTable : LongIdTable("instructors") {
    val firstName = varchar("first_name", 255)
    val lastName = varchar("last_name", 255)
    val biography = text("biography")
    val avatar = varchar("avatar", 255)
}