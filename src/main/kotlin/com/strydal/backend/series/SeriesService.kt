package com.strydal.backend.series

import com.strydal.backend.base.BaseService
import com.strydal.backend.base.ID
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
internal class SeriesService(private val seriesDao: SeriesDao) : BaseService<Series, SeriesWithID> {

    @Transactional(readOnly = false)
    override fun insert(entity: Series): ID = seriesDao.insert(entity)

    @Transactional(readOnly = false)
    override fun update(entity: SeriesWithID) = seriesDao.update(entity)

    @Transactional(readOnly = false)
    override fun deleteById(id: ID) = seriesDao.deleteById(id)

    override fun findAll(): List<SeriesWithID> = seriesDao.findAll()

    override fun findById(id: ID): SeriesWithID? = seriesDao.findById(id)

}