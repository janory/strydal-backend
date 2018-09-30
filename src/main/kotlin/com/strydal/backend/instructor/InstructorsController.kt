package com.strydal.backend.instructor

import com.strydal.backend.BaseController
import com.strydal.backend.ID
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
class InstructorsController(private val instructorService: InstructorService) :
    BaseController<TransInstructor, PersInstructor> {

    @PostMapping
    override fun insert(@RequestBody instructor: TransInstructor): ID =
        instructorService.insert(instructor)

    @PutMapping("/{id}")
    override fun update(@PathVariable("id") id: Long, @RequestBody instructor: PersInstructor) =
        instructorService.update(instructor.copy(id = id))


    @DeleteMapping("/{id}")
    override fun deleteById(@PathVariable("id") id: Long) =
        instructorService.deleteById(id)

    @GetMapping
    override fun findAll() =
        instructorService.findAll()

    @GetMapping("/{id}")
    override fun find(@PathVariable("id") id: Long) =
        instructorService.findById(id)
}