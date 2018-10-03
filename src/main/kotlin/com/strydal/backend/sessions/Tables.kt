package com.strydal.backend.sessions

import com.strydal.backend.instructor.Instructors
import com.strydal.backend.series.Series
import org.jetbrains.exposed.dao.LongIdTable

object Sessions : LongIdTable() {
    val series = reference("series_id", Series)
    val instructor = reference("instructor_id", Instructors)
    val approxDuration = long("approx_duration")
    val airDate = datetime("air_date")
    val artwork = varchar("artwork", 255)
    val liveUrl = varchar("live_url", 255)
    val archivedUrl = varchar("archived_url", 255)
}