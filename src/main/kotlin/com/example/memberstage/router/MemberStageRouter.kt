package com.example.memberstage.router

import com.example.memberstage.dto.MemberStageRequest
import com.example.memberstage.service.MemberStageService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Routing.memberStageRoute(memberStageService: MemberStageService) {
    route("api/v1") {
        authenticate {
            get("stages/{stageId}/rank") {
                val stageId = call.parameters.getOrFail<Long>("stageId")
                val ranking = memberStageService.getRankingToStage(stageId)
                call.respond(ranking)
            }
            put("stages/{stageId}/result") {
                val memberStageRequest = call.receive<MemberStageRequest>()
                val principal = call.principal<JWTPrincipal>()
                val updateMsg = memberStageService.saveOrUpdateMemberStageResult(principal!!, memberStageRequest)
                call.respond(updateMsg)
            }
            get("stages/{stageId}/members/{memberId}") {
                val stageId = call.parameters.getOrFail<Long>("stageId")
                val memberId = call.parameters.getOrFail<Long>("memberId")
                val memberStage = memberStageService.getMemberStage(stageId, memberId)
                call.respond(memberStage)
            }
        }
    }
}