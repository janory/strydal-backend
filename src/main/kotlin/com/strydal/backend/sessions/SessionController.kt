package com.strydal.backend.sessions

import com.strydal.backend.base.ID
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sessions", produces = [(MediaType.APPLICATION_JSON_VALUE)])
internal class SessionController(private val sessionService: SessionService) {

    @PostMapping
    fun insert(@RequestBody session: Session): ID =
        sessionService.insert(session)

    @PutMapping("/{id}")
    fun update(@PathVariable("id") id: Long, @RequestBody session: Session) =
        sessionService.update(SessionWithId(session, id))

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable("id") id: Long) =
        sessionService.deleteById(id)

    @GetMapping
    fun findAll() =
        sessionService.findAll()

    @GetMapping("/{id}")
    fun find(@PathVariable("id") id: Long) =
        sessionService.findById(id)
}