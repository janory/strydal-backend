package com.strydal.backend.instructor

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
@RequestMapping("/instructors", produces = [(MediaType.APPLICATION_JSON_VALUE)])
internal class InstructorController(private val instructorService: InstructorService) {

    @PostMapping
    fun insert(@RequestBody instructor: Instructor): ID =
        instructorService.insert(instructor)

    @PutMapping("/{id}")
    fun update(@PathVariable("id") id: Long, @RequestBody instructor: Instructor) =
        instructorService.update(InstructorWithId(instructor, id))


    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable("id") id: Long) =
        instructorService.deleteById(id)

    @GetMapping
    fun findAll() =
        instructorService.findAll()

    @GetMapping("/{id}")
    fun find(@PathVariable("id") id: Long) =
        instructorService.findById(id)
}
