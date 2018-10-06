package com.strydal.backend.sessions

import com.strydal.backend.base.BaseDao
import com.strydal.backend.base.DbRow
import com.strydal.backend.instructor.InstructorsTable
import com.strydal.backend.series.SeriesTable
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.springframework.stereotype.Component
import java.net.URL

@Component
internal class SessionDao : BaseDao<Session, SessionWithId>(SessionsTable) {
    override fun <T : LongIdTable> fromEntity(entity: Session): DbRow<T> = {
        it[SessionsTable.series] = EntityID(entity.seriesId, SeriesTable)
        it[SessionsTable.instructor] = EntityID(entity.instructorId, InstructorsTable)
        it[SessionsTable.approxDuration] = entity.approxDuration
        it[SessionsTable.airDate] = entity.airDate
        it[SessionsTable.artwork] = entity.artwork.toString()
        it[SessionsTable.liveUrl] = entity.liveUrl.toString()
        it[SessionsTable.archivedUrl] = entity.archivedUrl.toString()
    }

    override fun toEntity(row: ResultRow): SessionWithId =
        SessionWithId(
            Session(
                row[SessionsTable.series].value,
                row[SessionsTable.instructor].value,
                row[SessionsTable.approxDuration],
                row[SessionsTable.airDate],
                URL(row[SessionsTable.artwork]),
                URL(row[SessionsTable.liveUrl]),
                URL(row[SessionsTable.archivedUrl])
            ),
            row[SessionsTable.id].value
        )
}