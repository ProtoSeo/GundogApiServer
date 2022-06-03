package com.example.membercharacter.router

import com.example.membercharacter.dto.MemberCharacterLevelUpRequest
import com.example.membercharacter.service.MemberCharacterService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Routing.memberCharacterRoute(memberCharacterService: MemberCharacterService) {
    route("api/v1") {
        authenticate {
            get("characters") {
                val principal = call.principal<JWTPrincipal>()
                val memberCharacters = memberCharacterService.getMemberCharacters(principal!!)
                call.respond(memberCharacters)
            }
            post("characters/{charactersId}") {
                val charactersId = call.parameters.getOrFail<Long>("charactersId")
                val memberCharacter = memberCharacterService.openMemberCharacters(charactersId)
                call.respond(memberCharacter)
            }
            post("characters/{charactersId}/level") {
                val charactersId = call.parameters.getOrFail<Long>("charactersId")
                val request = call.receive<MemberCharacterLevelUpRequest>()
                val memberCharacter = memberCharacterService.levelUpMemberCharacters(charactersId, request)
                call.respond(memberCharacter)
            }
        }
    }
}