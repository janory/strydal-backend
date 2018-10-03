package com.strydal.backend.series

import com.strydal.backend.base.BaseDao
import com.strydal.backend.base.DbRow
import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.springframework.stereotype.Component
import java.net.URL

@Component
internal class SeriesDao : BaseDao<Series, SeriesWithID>(SeriesTable) {
    override fun <T : LongIdTable> fromEntity(entity: Series): DbRow<T> = {
        it[SeriesTable.title] = entity.title
        it[SeriesTable.description] = entity.description
        it[SeriesTable.artwork] = entity.artwork.toString()
    }

    override fun toEntity(row: ResultRow): SeriesWithID =
        SeriesWithID(
            Series(
                row[SeriesTable.title],
                row[SeriesTable.description],
                URL(
                    row[SeriesTable.artwork]
                )
            ),
            row[SeriesTable.id].value
        )
}