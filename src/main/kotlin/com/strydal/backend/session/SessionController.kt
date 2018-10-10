package com.strydal.backend.session

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonProperty.Access
import com.strydal.backend.base.ID
import com.strydal.backend.session.SessionView.Companion.fromView
import com.strydal.backend.session.SessionView.Companion.toView
import org.joda.time.DateTime
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URL

@RestController
@RequestMapping("/sessions", produces = [(MediaType.APPLICATION_JSON_VALUE)])
internal class SessionController(private val sessionService: SessionService) {

    @PostMapping
    fun insert(@RequestBody session: SessionView): ID =
        sessionService.insert(fromView(session))

    @PutMapping("/{id}")
    fun update(@PathVariable("id") id: Long, @RequestBody session: SessionView) =
        sessionService.update(SessionWithId(fromView(session), id))

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable("id") id: Long) =
        sessionService.deleteById(id)

    @GetMapping
    fun findAll() =
        sessionService.findAll().map(::toView)

    @GetMapping("/{id}")
    fun find(@PathVariable("id") id: Long): SessionView? {
        val session = sessionService.findById(id)
        return when (session) {
            null -> null
            else -> toView(session)
        }
    }
}

internal data class SessionView(
    @JsonProperty(access = Access.READ_ONLY)
    val id: Long?,
    val seriesId: Long,
    val instructorId: Long,
    val approxDuration: Long,
    val airDate: DateTime,
    val artwork: URL,
    val liveUrl: URL,
    val archivedUrl: URL
) {
    companion object {
        fun fromView(sess: SessionView) =
            Session(
                sess.seriesId,
                sess.instructorId,
                sess.approxDuration,
                sess.airDate,
                sess.artwork,
                sess.liveUrl,
                sess.archivedUrl
            )

        fun toView(sess: SessionWithId) =
            SessionView(
                sess.id,
                sess.entity.seriesId,
                sess.entity.instructorId,
                sess.entity.approxDuration,
                sess.entity.airDate,
                sess.entity.artwork,
                sess.entity.liveUrl,
                sess.entity.archivedUrl
            )
    }
}
