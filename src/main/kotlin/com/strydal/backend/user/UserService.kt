package com.strydal.backend.user

import com.strydal.backend.base.BaseService
import com.strydal.backend.base.ID
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
internal class UserService(private val userDao: UserDao) : BaseService<User, UserWithId> {

    @Transactional(readOnly = false)
    override fun insert(entity: User): ID = userDao.insert(entity).value

    @Transactional(readOnly = false)
    override fun update(entity: UserWithId) = userDao.update(entity)

    @Transactional(readOnly = false)
    override fun deleteById(id: ID) = userDao.deleteById(id)

    override fun findAll(): List<UserWithId> = userDao.findAll()

    override fun findById(id: ID): UserWithId? = userDao.findById(id)

    fun findByEmail(address: String): UserWithId? = userDao.findByEmail(address)

}
