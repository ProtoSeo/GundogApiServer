package com.example.stage.repository

import com.example.stage.domain.Stages
import com.example.stage.dto.Stage
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class StageRepository {
    init {
        transaction {
            SchemaUtils.create(Stages)
        }
    }

    fun findAll(): List<Stage> {
        return transaction {
            Stages.selectAll().orderBy(Stages.level).map { row ->
                Stage(row[Stages.id].value, row[Stages.name], row[Stages.level])
            }.toList()
        }
    }
}