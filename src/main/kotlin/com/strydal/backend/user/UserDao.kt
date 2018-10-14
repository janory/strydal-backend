package com.strydal.backend.user

import com.strydal.backend.base.BaseDao
import com.strydal.backend.base.DbRow
import com.strydal.backend.base.ID
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.springframework.stereotype.Component

@Component
internal class UserDao : BaseDao<UserWithPassword, UserWithId>(UsersTable) {

    fun findByEmail(address: String): UserWithId? {
        return UsersTable
            .select { UsersTable.email eq address.toLowerCase() }
            .map(::toEntity)
            .firstOrNull()
    }

    override fun <T : LongIdTable> fromEntity(user: UserWithPassword): DbRow<T> =
        {
            it[UsersTable.firstName] = user.entity.firstName
            it[UsersTable.lastName] = user.entity.lastName
            it[UsersTable.email] = user.entity.email.toLowerCase()
            it[UsersTable.password] = user.password
            it[UsersTable.birthday] = user.entity.birthday
            it[UsersTable.role] = user.entity.role
        }


    override fun toEntity(row: ResultRow): UserWithId =
        UserWithId(
            User(
                row[UsersTable.firstName],
                row[UsersTable.lastName],
                row[UsersTable.email],
                row[UsersTable.birthday],
                row[UsersTable.role]
            ),
            row[UsersTable.id].value
        )

}
