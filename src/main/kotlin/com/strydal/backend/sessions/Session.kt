package com.strydal.backend.sessions

import com.strydal.backend.base.Entity
import com.strydal.backend.base.EntityWithID
import com.strydal.backend.base.ID
import org.joda.time.DateTime
import java.net.URL

data class Session(
    val seriesId: Long,
    val instructorId: Long,
    val approxDuration: Long,
    val airDate: DateTime,
    val artwork: URL,
    val liveUrl: URL,
    val archivedUrl: URL
) : Entity

data class SessionWithId(
    override val entity: Session,
    override val id: ID
) : EntityWithID
