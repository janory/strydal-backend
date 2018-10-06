package com.strydal.backend.sessions

import com.strydal.backend.base.BaseService
import com.strydal.backend.base.ID
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
internal class SessionService(private val sessionDao: SessionDao) : BaseService<Session, SessionWithId> {

    @Transactional(readOnly = false)
    override fun insert(entity: Session): ID = sessionDao.insert(entity).value

    @Transactional(readOnly = false)
    override fun update(entity: SessionWithId) = sessionDao.update(entity)

    @Transactional(readOnly = false)
    override fun deleteById(id: ID) = sessionDao.deleteById(id)

    override fun findAll(): List<SessionWithId> = sessionDao.findAll()

    override fun findById(id: ID): SessionWithId? = sessionDao.findById(id)

}