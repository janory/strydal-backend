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
import org.jetbrains.exposed.sql.update
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class InstructorService : BaseService<TransInstructor, PersInstructor> {

    @Transactional(readOnly = false)
    override fun insert(entity: TransInstructor): ID =
        Instructors.insertAndGetId {
            it[lastName] = entity.lastName
            it[firstName] = entity.firstName
            it[biography] = entity.biography
            it[avatar] = entity.avatar
        }.value

    @Transactional(readOnly = false)
    override fun update(entity: PersInstructor) {
        Instructors.update({ Instructors.id eq entity.id }) {
            it[lastName] = entity.lastName
            it[firstName] = entity.firstName
            it[biography] = entity.biography
            it[avatar] = entity.avatar
        }
    }

    @Transactional(readOnly = false)
    override fun deleteById(id: ID) {
        Instructors.deleteWhere { Instructors.id eq id }
    }

    override fun findAll(): List<PersInstructor> =
        Instructors.selectAll()
            .map(::toInstructor)

    override fun findById(id: ID): PersInstructor? =
        Instructors.select { Instructors.id eq id }
            .map(::toInstructor).firstOrNull()


    companion object {
        fun toInstructor(row: ResultRow) =
            PersInstructor(
                row[Instructors.lastName],
                row[Instructors.firstName],
                row[Instructors.biography],
                row[Instructors.avatar],
                row[Instructors.id].value
            )
    }
}

data class TransInstructor(
    val firstName: String,
    val lastName: String,
    val biography: String,
    val avatar: String
) : TransientEntity

data class PersInstructor(
    val firstName: String,
    val lastName: String,
    val biography: String,
    val avatar: String,
    override val id: ID
) : PersistentEntity