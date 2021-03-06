package com.example.memberstage.router

import com.example.member.exception.MemberException
import com.example.member.exception.MemberExceptionType.*
import com.example.memberstage.dto.StageClearRequestDto
import com.example.memberstage.dto.StageInfoDto
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
            get("stages") {
                val principal = call.principal<JWTPrincipal>() ?: throw MemberException(UN_AUTHORIZED)
                val stages = memberStageService.getStages(principal)
                call.respond(stages)
            }
            get("stages/{stageId}/rank") {
                val stageId = call.parameters.getOrFail<Long>("stageId")
                val ranking = memberStageService.getRankingToStage(stageId)
                call.respond(ranking)
            }
            put("stages/{stageId}/result") {
                val memberStageRequest = call.receive<StageClearRequestDto>()
                val principal = call.principal<JWTPrincipal>() ?: throw MemberException(UN_AUTHORIZED)
                val memberStage = memberStageService.updateMemberStageResult(principal, memberStageRequest)
                call.respond(memberStage)
            }
            get("stages/{stageId}") {
                val principal = call.principal<JWTPrincipal>() ?: throw MemberException(UN_AUTHORIZED)
                val stageId = call.parameters.getOrFail<Long>("stageId")
                val memberStage = memberStageService.getMemberStage(principal, stageId)
                call.respond(memberStage)
            }
            get("stages/{stageId}/info") {
                val principal = call.principal<JWTPrincipal>() ?: throw MemberException(UN_AUTHORIZED)
                val stageId = call.parameters.getOrFail<Long>("stageId")
                val memberStage = memberStageService.getMemberStage(principal, stageId)
                val ranking = memberStageService.getRankingToStage(stageId)
                call.respond(StageInfoDto(ranking, memberStage))
            }
        }
    }
}