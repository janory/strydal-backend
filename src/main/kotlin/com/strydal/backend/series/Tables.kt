package com.strydal.backend.series

import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject

object SeriesTable : LongIdTable("seriesId") {
    val title = varchar("title", 255)
    val description = text("description")
    val artwork = varchar("artwork", 255)
}

object SeriesCategoriesTable : Table("series_categories_connect") {
    val seriesId = reference("series_id", SeriesTable)
    val category = customEnumeration(
        "category",
        "CategoryEnum",
        { value -> Category.valueOf(value as String) },
        { it -> PGEnum("CategoryEnum", it) })

    init {
        uniqueIndex(seriesId, category)
    }

}

enum class Category { CARDIO, STRENGTH, FLEXIBILITY, MINDFULNESS }

class PGEnum<T : Enum<T>>(enumTypeName: String, enumValue: T?) : PGobject() {
    init {
        value = enumValue?.name
        type = enumTypeName
    }
}