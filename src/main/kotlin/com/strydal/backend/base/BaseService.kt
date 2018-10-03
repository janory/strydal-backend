package com.strydal.backend.base

internal interface BaseService<TE: Entity, PE: EntityWithID> {

    fun insert(entity: TE): ID

    fun update(entity: PE)

    fun deleteById(id: ID)

    fun findAll(): List<PE>

    fun findById(id: ID): PE?

}