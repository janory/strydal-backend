package com.strydal.backend.series

import com.strydal.backend.BaseService
import com.strydal.backend.ID
import com.strydal.backend.PersistentEntity
import com.strydal.backend.TransientEntity
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.update
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.net.URL

typealias DbRow = SeriesTable.(UpdateBuilder<Int>) -> Unit


@Service
@Transactional(readOnly = true)
class SeriesService : BaseService<TransSeries, PersSeries> {

    @Transactional(readOnly = false)
    override fun insert(entity: TransSeries): ID =
        SeriesTable.insertAndGetId(fromSeries(entity)).value

    @Transactional(readOnly = false)
    override fun update(entity: PersSeries) {
        SeriesTable.update({ SeriesTable.id eq entity.id }) {
            it[SeriesTable.title] = entity.title
            it[SeriesTable.description] = entity.description
            it[SeriesTable.artwork] = entity.artwork.toString()
        }
    }

    @Transactional(readOnly = false)
    override fun deleteById(id: ID) {
        SeriesTable.deleteWhere { SeriesTable.id eq id }
    }

    override fun findAll(): List<PersSeries> =
        SeriesTable.selectAll()
            .map(::toSeries)

    override fun findById(id: ID): PersSeries? =
        SeriesTable.select { SeriesTable.id eq id }
            .map(::toSeries).firstOrNull()

    companion object {
        private fun fromSeries(entity: Series): DbRow =
            {
                it[title] = entity.title
                it[description] = entity.description
                it[artwork] = entity.artwork.toString()
            }


        private fun toSeries(row: ResultRow) =
            PersSeries(
                row[SeriesTable.title],
                row[SeriesTable.description],
                URL(row[SeriesTable.artwork]),
                row[SeriesTable.id].value
            )
    }

}

private interface Series {
    val title: String
    val description: String
    val artwork: URL
}

data class PersSeries(
    override val title: String,
    override val description: String,
    override val artwork: URL,
    override val id: ID
) : Series, PersistentEntity

data class TransSeries(
    override val title: String,
    override val description: String,
    override val artwork: URL
) : Series, TransientEntity