package com.strydal.backend.series

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
@RequestMapping("/series", produces = [(MediaType.APPLICATION_JSON_VALUE)])
internal class SeriesController(private val seriesService: SeriesService) {

    @PostMapping
    fun insert(@RequestBody series: Series): ID =
        seriesService.insert(series)

    @PutMapping("/{id}")
    fun update(@PathVariable("id") id: Long, @RequestBody series: Series) =
        seriesService.update(SeriesWithID(series, id))


    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable("id") id: Long) =
        seriesService.deleteById(id)

    @GetMapping
    fun findAll() = seriesService.findAll()

    @GetMapping("/{id}")
    fun find(@PathVariable("id") id: Long) = seriesService.findById(id)

}