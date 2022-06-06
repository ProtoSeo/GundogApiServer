package com.example.stage.router

import com.example.stage.service.StageService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.stageRouter(stageService: StageService) {
    route("api/v1") {
        authenticate {
            get("stages") {
                val stages = stageService.getAllStage()
                call.respond(stages)
            }
        }
    }
}