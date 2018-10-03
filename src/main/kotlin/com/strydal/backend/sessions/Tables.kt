package com.strydal.backend.sessions

import com.strydal.backend.instructor.InstructorsTable
import com.strydal.backend.series.SeriesTable
import org.jetbrains.exposed.dao.LongIdTable

object SessionsTable : LongIdTable("sessions") {
    val series = reference("series_id", SeriesTable)
    val instructor = reference("instructor_id", InstructorsTable)
    val approxDuration = long("approx_duration")
    val airDate = datetime("air_date")
    val artwork = varchar("artwork", 255)
    val liveUrl = varchar("live_url", 255)
    val archivedUrl = varchar("archived_url", 255)
}