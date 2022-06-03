package com.example.membercharacter.dto

data class MemberCharacterResponse(
    val id: Long,
    val characterId: Long,
    val healthLevel: Int,
    val staminaLevel: Int,
    val isOpen: Boolean,
)
