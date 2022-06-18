package com.example.common.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import javax.sql.DataSource

fun Application.configureDB() {
    val databaseObject = environment.config.config("ktor.database")
    val config = HikariConfig().apply {
        jdbcUrl = databaseObject.property("url").getString()
        driverClassName = databaseObject.property("driver").getString()
        username = databaseObject.property("user").getString()
        password = databaseObject.property("password").getString()
        maximumPoolSize = 10
    }
    val dataSource = HikariDataSource(config)
    databaseMigration(dataSource)
    Database.connect(dataSource)
}

fun databaseMigration(dataSource: DataSource) {
    val flyway = Flyway.configure().dataSource(dataSource).load()
    flyway.migrate()
}