package com.strydal.backend.instructor

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

typealias DbRow = InstructorsTable.(UpdateBuilder<Int>) -> Unit

@Service
@Transactional(readOnly = true)
class InstructorService : BaseService<TransInstructor, PersInstructor> {

    @Transactional(readOnly = false)
    override fun insert(entity: TransInstructor): ID =
        InstructorsTable.insertAndGetId(fromInstructor(entity)).value

    @Transactional(readOnly = false)
    override fun update(entity: PersInstructor) {
        InstructorsTable.update(where = { InstructorsTable.id eq entity.id }, body = fromInstructor(entity))
    }

    @Transactional(readOnly = false)
    override fun deleteById(id: ID) {
        InstructorsTable.deleteWhere { InstructorsTable.id eq id }
    }

    override fun findAll(): List<PersInstructor> =
        InstructorsTable.selectAll()
            .map(::toInstructor)

    override fun findById(id: ID): PersInstructor? =
        InstructorsTable.select { InstructorsTable.id eq id }
            .map(::toInstructor).firstOrNull()


    companion object {
        private fun fromInstructor(entity: Instructor): DbRow =
            {
                it[InstructorsTable.lastName] = entity.lastName
                it[InstructorsTable.firstName] = entity.firstName
                it[InstructorsTable.biography] = entity.biography
                it[InstructorsTable.avatar] = entity.avatar
            }


        private fun toInstructor(row: ResultRow) =
            PersInstructor(
                row[InstructorsTable.lastName],
                row[InstructorsTable.firstName],
                row[InstructorsTable.biography],
                row[InstructorsTable.avatar],
                row[InstructorsTable.id].value
            )
    }
}

private interface Instructor {
    val firstName: String
    val lastName: String
    val biography: String
    val avatar: String
}

data class TransInstructor(
    override val firstName: String,
    override val lastName: String,
    override val biography: String,
    override val avatar: String
) : Instructor, TransientEntity

data class PersInstructor(
    override val firstName: String,
    override val lastName: String,
    override val biography: String,
    override val avatar: String,
    override val id: ID
) : Instructor, PersistentEntity