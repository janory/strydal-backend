package com.strydal.backend

import com.fasterxml.jackson.databind.Module
import org.jetbrains.exposed.spring.SpringTransactionManager
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import javax.sql.DataSource
import com.fasterxml.jackson.datatype.joda.JodaModule
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder


@SpringBootApplication
class BackendApplication {
    @Bean
    fun springTransactionManager(dataSource: DataSource) = SpringTransactionManager(dataSource)

    @Bean
    fun jodaModule(): Module {
        return JodaModule()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}

fun main(args: Array<String>) {
    runApplication<BackendApplication>(*args)
}