package com.strydal.backend

interface BaseController<TransientEntity, PersistentEntity> {

    fun insert(entity: TransientEntity): ID

    fun update(id: ID, entity: PersistentEntity)

    fun deleteById(id: ID)

    fun findAll(): List<PersistentEntity>

    fun find(id: ID): PersistentEntity?

}