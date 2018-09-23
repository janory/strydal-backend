package com.strydal.backend.model

import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject

object Instructors : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val firstName = varchar("first_name", 255)
    val lastName = varchar("last_name", 255)
    val biography = text("biography")
    val avatar = varchar("avatar", 255)
}

object Series : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val title = varchar("title", 255)
    val description = text("description")
    val artwork = varchar("artwork", 255)
}

object Sessions : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val seriesId = (integer("series_id") references Series.id)
    val instructorId = (integer("instructor_id") references Instructors.id)
    val approxDuration = long("approx_duration")
    val airDate = datetime("air_date")
    val artwork = varchar("artwork", 255)
    val liveUrl = varchar("live_url", 255)
    val archivedUrl = varchar("archived_url", 255)
}

object CategoriesConnect : Table() {
    val seriesId = (integer("series_id") references Series.id).uniqueIndex()
    val categories = customEnumeration(
        "categories",
        "CategoryEnum",
        { value -> Category.valueOf(value as String) },
        { it -> PGEnum("CategoryEnum", it) })
}

enum class Category { CARDIO, STRENGTH, FLEXIBILITY, MINDFULNESS }

class PGEnum<T : Enum<T>>(enumTypeName: String, enumValue: T?) : PGobject() {
    init {
        value = enumValue?.name
        type = enumTypeName
    }
}