package com.strydal.backend.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/", produces = [(MediaType.APPLICATION_JSON_VALUE)])
class HelloController {

    @GetMapping
    fun hello() =
        Session(
            "First series",
            "Gui",
            "30 mins",
            "Yesterday",
            "http://art.work",
            "finished",
            "http://viemo.com/4324234"
        )


    data class Session(
        val series: String,
        val instructor: String,
        val approx_duration: String,
        val air_date: String,
        val artwork: String,
        val live_url: String,
        val archived_url: String
    )
}