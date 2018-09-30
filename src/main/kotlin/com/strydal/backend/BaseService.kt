package com.strydal.backend

typealias ID = Long

interface BaseService<TransientEntity, PersistentEntity> {

    fun insert(entity: TransientEntity): ID

    fun update(entity: PersistentEntity)

    fun deleteById(id: ID)

    fun findAll(): List<PersistentEntity>

    fun findById(id: ID): PersistentEntity?

}

interface PersistentEntity {
    val id: ID
}

interface TransientEntity