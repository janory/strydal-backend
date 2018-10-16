package com.strydal.backend.user

import com.strydal.backend.base.BaseDao
import com.strydal.backend.base.DbRow
import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.springframework.stereotype.Component

@Component
internal class UserDao : BaseDao<User, UserWithId>(UsersTable) {

    fun findByEmail(address: String): UserWithId? {
        return UsersTable
            .select { UsersTable.email eq address.toLowerCase() }
            .map(::toEntity)
            .firstOrNull()
    }

    override fun <T : LongIdTable> fromEntity(entity: User): DbRow<T> =
        {
            it[UsersTable.firstName] = entity.firstName
            it[UsersTable.lastName] = entity.lastName
            it[UsersTable.email] = entity.email.toLowerCase()
            it[UsersTable.password] = entity.password
            it[UsersTable.birthday] = entity.birthday
            it[UsersTable.role] = entity.role
        }


    override fun toEntity(row: ResultRow): UserWithId =
        UserWithId(
            User(
                row[UsersTable.firstName],
                row[UsersTable.lastName],
                row[UsersTable.email],
                row[UsersTable.password],
                row[UsersTable.birthday],
                row[UsersTable.role]
            ),
            row[UsersTable.id].value
        )

}
