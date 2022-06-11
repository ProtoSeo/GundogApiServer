package com.example.membercharacter.router

import com.example.member.exception.MemberException
import com.example.member.exception.MemberExceptionType.*
import com.example.membercharacter.service.MemberCharacterService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Routing.memberCharacterRoute(memberCharacterService: MemberCharacterService) {
    route("api/v1") {
        authenticate {
            get("characters") {
                val principal = call.principal<JWTPrincipal>() ?: throw MemberException(UN_AUTHORIZED)
                val memberCharacters = memberCharacterService.getMemberCharacters(principal)
                call.respond(memberCharacters)
            }
            post("characters/{charactersId}") {
                val principal = call.principal<JWTPrincipal>() ?: throw MemberException(UN_AUTHORIZED)
                val charactersId = call.parameters.getOrFail<Long>("charactersId")
                val memberCharacter = memberCharacterService.unlockMemberCharacters(principal, charactersId)
                call.respond(memberCharacter)
            }
            post("characters/{charactersId}/health") {
                val principal = call.principal<JWTPrincipal>() ?: throw MemberException(UN_AUTHORIZED)
                val charactersId = call.parameters.getOrFail<Long>("charactersId")
                val memberCharacter = memberCharacterService.levelUpHealthLevel(principal, charactersId)
                call.respond(memberCharacter)
            }
            post("characters/{charactersId}/stamina") {
                val principal = call.principal<JWTPrincipal>() ?: throw MemberException(UN_AUTHORIZED)
                val charactersId = call.parameters.getOrFail<Long>("charactersId")
                val memberCharacter = memberCharacterService.levelUpStaminaLevel(principal, charactersId)
                call.respond(memberCharacter)
            }
        }
    }
}