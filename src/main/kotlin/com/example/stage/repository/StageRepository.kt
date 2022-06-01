package com.example.stage.repository

import com.example.stage.domain.Stages
import com.example.stage.dto.StageResponse
import com.example.stage.dto.StageSaveRequest
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class StageRepository {
    init {
        transaction {
            SchemaUtils.create(Stages)
        }
    }

    fun save(request: StageSaveRequest): Long {
        return transaction {
            Stages.insertAndGetId { row ->
                row[name] = request.name
                row[level] = request.level
            }
        }.value
    }

    fun findAll(): List<StageResponse> {
        return transaction {
            Stages.selectAll().orderBy(Stages.level).map { row ->
                StageResponse(row[Stages.id].value, row[Stages.name], row[Stages.level])
            }.toList()
        }
    }
}