package com.strydal.backend

typealias ID = Long

interface BaseService<TE: TransientEntity, PE: PersistentEntity> {

    fun insert(entity: TE): ID

    fun update(entity: PE)

    fun deleteById(id: ID)

    fun findAll(): List<PE>

    fun findById(id: ID): PE?

}

interface PersistentEntity {
    val id: ID
}

interface TransientEntity