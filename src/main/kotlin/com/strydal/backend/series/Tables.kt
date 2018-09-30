package com.strydal.backend.series

import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject

object Series : LongIdTable() {
    val title = varchar("title", 255)
    val description = text("description")
    val artwork = varchar("artwork", 255)
}

object CategoriesConnect : Table() {
    val series = reference("series_id", Series).uniqueIndex()
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