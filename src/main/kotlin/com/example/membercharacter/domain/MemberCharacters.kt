package com.example.membercharacter.domain

import com.example.member.domain.Members
import org.jetbrains.exposed.dao.id.LongIdTable

object MemberCharacters : LongIdTable("member_character") {
    val memberId = reference("member_id", Members)
    val characterId = reference("character_id", Characters)
    val healthLevel = integer("health_level").default(0)
    val staminaLevel = integer("stamina_level").default(0)
    val isOpen = bool("is_open").default(false)
}