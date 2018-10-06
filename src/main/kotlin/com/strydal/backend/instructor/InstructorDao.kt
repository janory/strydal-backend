package com.strydal.backend.instructor

import com.strydal.backend.base.BaseDao
import com.strydal.backend.base.DbRow
import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.springframework.stereotype.Component

@Component
internal class InstructorDao : BaseDao<Instructor, InstructorWithId>(InstructorsTable) {
    override fun <T : LongIdTable> fromEntity(entity: Instructor): DbRow<T> =
        {
            it[InstructorsTable.lastName] = entity.lastName
            it[InstructorsTable.firstName] = entity.firstName
            it[InstructorsTable.biography] = entity.biography
            it[InstructorsTable.avatar] = entity.avatar
        }


    override fun toEntity(row: ResultRow): InstructorWithId =
        InstructorWithId(
            Instructor(
                row[InstructorsTable.lastName],
                row[InstructorsTable.firstName],
                row[InstructorsTable.biography],
                row[InstructorsTable.avatar]
            ),
            row[InstructorsTable.id].value
        )

}
