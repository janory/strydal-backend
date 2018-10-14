package com.strydal.backend.base

import org.springframework.web.util.UriComponentsBuilder
import javax.servlet.http.HttpServletRequest

internal fun buildLocation(path: String, request: HttpServletRequest) =
    UriComponentsBuilder.newInstance()
        .scheme(request.scheme)
        .host(request.serverName)
        .port(request.serverPort)
        .path(path)
        .build()
        .toUri()
