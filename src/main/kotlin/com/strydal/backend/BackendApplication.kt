package com.strydal.backend

import com.strydal.backend.model.CategoriesConnect
import com.strydal.backend.model.Instructors
import com.strydal.backend.model.Series
import com.strydal.backend.model.Sessions
import org.jetbrains.exposed.spring.SpringTransactionManager
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.springframework.beans.factory.InitializingBean
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionTemplate
import javax.sql.DataSource

@SpringBootApplication
class BackendApplication {
    @Bean
    fun springTransactionManager(dataSource: DataSource) = SpringTransactionManager(dataSource)

    @Bean
    fun insertData(instructorService: InstructorService) = ApplicationRunner {
        instructorService.insert(Instructor("Janos", "Szathmary", "Just a guy", "Nope"))
    }
}

fun main(args: Array<String>) {
    runApplication<BackendApplication>(*args)
}

@Component
class DbSetup(private val transactionTemplate: TransactionTemplate) : InitializingBean {
    override fun afterPropertiesSet() {
        transactionTemplate.execute {
//            with(TransactionManager.current()) {
//                exec("CREATE TYPE CategoryEnum AS ENUM ('CARDIO', 'STRENGTH', 'FLEXIBILITY', 'MINDFULNESS');")
//                commit()
//            }
            SchemaUtils.create(Instructors, Series, Sessions, CategoriesConnect)
        }
    }
}

data class Instructor(
    val firstName: String,
    val lastName: String,
    val biography: String,
    val avatar: String
)

@Service
@Transactional(readOnly = true)
class InstructorService {

    @Transactional(readOnly = false)
    fun insert(i: Instructor) {
        Instructors.insert {
            it[Instructors.lastName] = i.lastName
            it[Instructors.firstName] = i.firstName
            it[Instructors.biography] = i.biography
            it[Instructors.avatar] = i.avatar
        }
    }
}
