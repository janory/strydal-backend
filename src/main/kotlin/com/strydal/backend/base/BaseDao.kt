package com.strydal.backend.base

import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.update

typealias DbRow<T> = T.(UpdateBuilder<Int>) -> Unit
typealias ID = Long

interface Entity

interface EntityWithID : Entity {
    val entity: Entity
    val id: ID
}

internal abstract class BaseDao<E : Entity, EWID : EntityWithID>(private val table: LongIdTable) {

    abstract fun <T : LongIdTable> fromEntity(entity: E): DbRow<T>

    abstract fun toEntity(row: ResultRow): EWID

    fun insert(entity: E): ID =
        table.insertAndGetId(fromEntity(entity)).value

    fun update(entity: EWID) {
        table.update(where = { table.id eq entity.id }, body = fromEntity(entity.entity as E))
    }

    fun deleteById(id: ID) {
        table.deleteWhere { table.id eq id }
    }

    fun findAll(): List<EWID> =
        table.selectAll().map(::toEntity)

    fun findById(id: ID): EWID? =
        table.select { table.id eq id }
            .map(::toEntity).firstOrNull()

}

