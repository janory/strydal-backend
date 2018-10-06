package com.strydal.backend.series

import com.strydal.backend.base.ID
import com.strydal.backend.series.SeriesCategoriesTable.category
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.net.URL

@Service
@Transactional(readOnly = true)
internal class SeriesService(private val seriesDao: SeriesDao) {

    @Transactional(readOnly = false)
    fun insert(view: SeriesView): ID {
        val entity = Series(
            view.title,
            view.description,
            view.artwork
        )
        val id = seriesDao.insert(entity)
        view.categories.forEach { cgy ->
            SeriesCategoriesTable.insert {
                it[seriesId] = id
                it[category] = cgy
            }
        }
        return id.value
    }

    @Transactional(readOnly = false)
    fun update(view: SeriesViewWithId) {
        val entity = SeriesWithId(
            Series(
                view.title,
                view.description,
                view.artwork
            ),
            view.id
        )
        seriesDao.update(entity)
        val newCategories: Set<Category> = view.categories
        val currentCategories: Set<Category> =
            SeriesCategoriesTable.select { SeriesCategoriesTable.seriesId eq entity.id }
                .map {
                    it[category]
                }
                .toSet()

        val categoriesToDelete = currentCategories - newCategories
        val categoriesToAdd = newCategories - currentCategories
        categoriesToDelete.forEach { ctg ->
            SeriesCategoriesTable.deleteWhere {
                SeriesCategoriesTable.seriesId.eq(entity.id) and
                        SeriesCategoriesTable.category.eq(ctg)
            }
        }
        categoriesToAdd.map { ctg ->
            SeriesCategoriesTable.insert {
                it[seriesId] = EntityID(entity.id, SeriesTable)
                it[category] = ctg
            }
        }

    }

    @Transactional(readOnly = false)
    fun deleteById(id: ID) {
        SeriesCategoriesTable.deleteWhere {
            SeriesCategoriesTable.seriesId eq id
        }
        seriesDao.deleteById(id)
    }

    fun findAll(): List<SeriesViewWithId> {
        val query: Query = (SeriesTable innerJoin SeriesCategoriesTable)
            .selectAll()

        return query.fold(mutableMapOf<Long, SeriesViewWithId>()) { mapOfSeries, row ->
            val seriesId = row[SeriesCategoriesTable.seriesId].value
            val category = row[SeriesCategoriesTable.category]
            if (mapOfSeries.containsKey(seriesId)) {
                val seriesViewWithId = mapOfSeries[seriesId]!!
                mapOfSeries[seriesId] = seriesViewWithId.copy(
                    categories = seriesViewWithId.categories + category
                )
            } else {
                val seriesViewWithId = SeriesViewWithId(
                    row[SeriesTable.id].value,
                    row[SeriesTable.title],
                    row[SeriesTable.description],
                    URL(row[SeriesTable.artwork]),
                    setOf(category)
                )
                mapOfSeries[seriesId] = seriesViewWithId
            }
            mapOfSeries
        }.values.toList()
    }


    fun findById(id: ID): SeriesViewWithId? {
        val series = seriesDao.findById(id)
        return when (series) {
            null -> null
            else -> {
                val categories = SeriesCategoriesTable
                    .select { SeriesCategoriesTable.seriesId eq id }
                    .map { it[category] }
                    .toSet()

                SeriesViewWithId(
                    series.id,
                    series.entity.title,
                    series.entity.description,
                    series.entity.artwork,
                    categories
                )
            }
        }
    }

}