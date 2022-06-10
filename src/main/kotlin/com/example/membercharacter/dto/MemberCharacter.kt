package com.example.membercharacter.dto

data class MemberCharacter(
    val id: Long,
    val characterId: Long,
    val healthLevel: Int,
    val staminaLevel: Int,
    val isOpen: Boolean,
)
