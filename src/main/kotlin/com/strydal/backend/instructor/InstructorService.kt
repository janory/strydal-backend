package com.strydal.backend.instructor

import com.strydal.backend.base.BaseService
import com.strydal.backend.base.ID
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
internal class InstructorService(private val instructorDao: InstructorDao) :
    BaseService<Instructor, InstructorWithID> {

    @Transactional(readOnly = false)
    override fun insert(entity: Instructor): ID = instructorDao.insert(entity).value

    @Transactional(readOnly = false)
    override fun update(entity: InstructorWithID) = instructorDao.update(entity)

    @Transactional(readOnly = false)
    override fun deleteById(id: ID) = instructorDao.deleteById(id)

    override fun findAll(): List<InstructorWithID> = instructorDao.findAll()

    override fun findById(id: ID): InstructorWithID? = instructorDao.findById(id)

}
