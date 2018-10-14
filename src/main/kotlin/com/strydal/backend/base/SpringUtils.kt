package com.strydal.backend.base

import org.springframework.web.util.UriComponentsBuilder
import javax.servlet.http.HttpServletRequest

internal data class ErrorResponse(val error: String)

internal fun buildLocation(id: Long, request: HttpServletRequest) =
    UriComponentsBuilder.newInstance()
        .scheme(request.scheme)
        .host(request.serverName)
        .port(request.serverPort)
        .path("${request.servletPath}/$id")
        .build()
        .toUri()
