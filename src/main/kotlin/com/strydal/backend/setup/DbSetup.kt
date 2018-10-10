package com.strydal.backend.setup

import com.strydal.backend.instructor.InstructorsTable
import com.strydal.backend.series.SeriesCategoriesTable
import com.strydal.backend.series.SeriesTable
import com.strydal.backend.session.SessionsTable
import com.strydal.backend.user.UsersTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.springframework.beans.factory.InitializingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionTemplate

@Component
@ConditionalOnProperty(name = ["spring.datasource.generate-ddl"], havingValue = "true")
class Setup(private val transactionTemplate: TransactionTemplate) : InitializingBean {
    override fun afterPropertiesSet() {
        transactionTemplate.execute {
            with(TransactionManager.current()) {
//                exec("CREATE TYPE CategoryEnum AS ENUM ('CARDIO', 'STRENGTH', 'FLEXIBILITY', 'MINDFULNESS');")
//                exec("CREATE TYPE USerRoleEnum AS ENUM ('USER', 'ADMIN');")
                commit()
            }
            SchemaUtils.create(InstructorsTable, SeriesTable, SessionsTable, SeriesCategoriesTable, UsersTable)
        }
    }
}