package com.strydal.backend.user

import arrow.core.Either
import com.strydal.backend.base.ID
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
internal class UserService(private val userDao: UserDao) {

    @Transactional(readOnly = false)
    fun insert(user: UserWithPassword) =
        Either.cond(
            userDao.findByEmail(user.entity.email) == null,
            { userDao.insert(user).value },
            { Error.EmailAlreadyRegistered })

    @Transactional(readOnly = false)
    fun update(entity: UserWithId) = userDao.update(entity)

    @Transactional(readOnly = false)
    fun deleteById(id: ID) = userDao.deleteById(id)

    fun findAll(): List<UserWithId> = userDao.findAll()

    fun findById(id: ID): UserWithId? = userDao.findById(id)
}
