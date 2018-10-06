package com.strydal.backend.instructor

import com.strydal.backend.base.ID
import com.strydal.backend.instructor.InstructorView.Companion.fromView
import com.strydal.backend.instructor.InstructorView.Companion.toView
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
    fun insert(@RequestBody instructor: InstructorView): ID =
        instructorService.insert(fromView(instructor))

    @PutMapping("/{id}")
    fun update(@PathVariable("id") id: Long, @RequestBody instructor: InstructorView) =
        instructorService.update(
            InstructorWithId(fromView(instructor), id)
        )

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable("id") id: Long) =
        instructorService.deleteById(id)

    @GetMapping
    fun findAll() =
        instructorService.findAll().map(::toView)

    @GetMapping("/{id}")
    fun find(@PathVariable("id") id: Long): InstructorView? {
        val instructor = instructorService.findById(id)
        return when (instructor) {
            null -> null
            else -> toView(instructor)
        }
    }
}

internal data class InstructorView(
    val id: Long?,
    val firstName: String,
    val lastName: String,
    val biography: String,
    val avatar: String
) {
    companion object {
        fun fromView(inst: InstructorView) =
            Instructor(
                inst.firstName,
                inst.lastName,
                inst.biography,
                inst.avatar
            )

        fun toView(inst: InstructorWithId) =
            InstructorView(
                inst.id,
                inst.entity.firstName,
                inst.entity.lastName,
                inst.entity.biography,
                inst.entity.avatar
            )
    }
}
