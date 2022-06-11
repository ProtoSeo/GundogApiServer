package com.example.membercharacter.service

import com.example.membercharacter.domain.LevelUpValue.*
import com.example.membercharacter.dto.MemberCharacter
import com.example.membercharacter.exception.MemberCharacterException
import com.example.membercharacter.exception.MemberCharacterExceptionType.*
import com.example.membercharacter.repository.MemberCharacterRepository
import io.ktor.server.auth.jwt.*

class MemberCharacterService(private val memberCharacterRepository: MemberCharacterRepository) {

    fun getMemberCharacters(principal: JWTPrincipal): List<MemberCharacter> {
        val memberId = principal.payload.getClaim("id").asLong()
        return memberCharacterRepository.findAllByMemberId(memberId)
    }

    fun levelUpStaminaLevel(principal: JWTPrincipal, memberCharacterId: Long): MemberCharacter {
        val memberId = principal.payload.getClaim("id").asLong()
        return memberCharacterRepository.update(memberId, memberCharacterId, STAMINA)
            ?: throw MemberCharacterException(NOT_FOUND)
    }

    fun levelUpHealthLevel(principal: JWTPrincipal, memberCharacterId: Long): MemberCharacter {
        val memberId = principal.payload.getClaim("id").asLong()
        return memberCharacterRepository.update(memberId, memberCharacterId, HEALTH)
            ?: throw MemberCharacterException(NOT_FOUND)
    }

    fun unlockMemberCharacters(principal: JWTPrincipal, memberCharacterId: Long): MemberCharacter {
        val memberId = principal.payload.getClaim("id").asLong()
        return memberCharacterRepository.update(memberId, memberCharacterId)
            ?: throw MemberCharacterException(NOT_FOUND)
    }
}