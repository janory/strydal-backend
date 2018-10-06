package com.strydal.backend.series

import com.strydal.backend.base.Entity
import com.strydal.backend.base.EntityWithID
import com.strydal.backend.base.ID
import java.net.URL

data class Series(
    val title: String,
    val description: String,
    val artwork: URL
) : Entity

data class SeriesWithId(
    override val entity: Series,
    override val id: ID
) : EntityWithID
