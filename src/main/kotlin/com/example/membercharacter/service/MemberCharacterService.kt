package com.example.membercharacter.service

import com.example.membercharacter.dto.MemberCharacterLevelUpRequest
import com.example.membercharacter.dto.MemberCharacterResponse
import com.example.membercharacter.repository.MemberCharacterRepository
import io.ktor.server.auth.jwt.*

class MemberCharacterService(private val memberCharacterRepository: MemberCharacterRepository) {

    fun getMemberCharacters(principal: JWTPrincipal): List<MemberCharacterResponse> {
        val memberId = principal.payload.getClaim("id").asLong()
        return memberCharacterRepository.findAllByMemberId(memberId)
    }

    fun levelUpMemberCharacters(memberCharacterId: Long, request: MemberCharacterLevelUpRequest): MemberCharacterResponse {
        return memberCharacterRepository.update(memberCharacterId, request)
    }

    fun openMemberCharacters(memberCharacterId: Long): MemberCharacterResponse {
        return memberCharacterRepository.update(memberCharacterId)
    }
}