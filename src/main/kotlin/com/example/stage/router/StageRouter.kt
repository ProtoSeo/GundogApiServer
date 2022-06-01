package com.example.stage.router

import com.example.stage.dto.StageSaveRequest
import com.example.stage.service.StageService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.stageRouter(stageService: StageService) {
    route("api/v1") {
        post("stages") {
            val memberRequest = call.receive<StageSaveRequest>()
            val id = stageService.saveStage(memberRequest)
            call.respond(status = HttpStatusCode.Created, id)
        }
        get("stages") {
            val stages = stageService.getAllStage()
            call.respond(stages)
        }
    }
}