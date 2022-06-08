package com.example.membercharacter.domain

import com.example.membercharacter.domain.LevelLimit.*

enum class LevelLimit(val limit: Int) {
    HEALTH_LEVEL(6), STAMINA_LEVEL(7);
}

enum class LevelUpValue {
    STAMINA, HEALTH
}

object ItemLimit {
    private val healthLevelUpWantDogGum = longArrayOf(3, 6, 9, 18, 27, 27, 27)
    private val staminaLevelUpWantDogGum = longArrayOf(3, 6, 9, 18, 27, 27, 27, 27)
    private val secondCharacterIsOpen = mapOf(2L to 4, 3L to 4)
    private val thirdCharacterIsOpen = mapOf(4L to 4, 5L to 4)

    fun numberOfDogGumForHealthLevelUp(nowLevel: Int) =
        if (nowLevel <= HEALTH_LEVEL.limit) healthLevelUpWantDogGum[nowLevel] else -1L

    fun numberOfDogGumForStaminaLevelUp(nowLevel: Int) =
        if (nowLevel <= STAMINA_LEVEL.limit) staminaLevelUpWantDogGum[nowLevel] else -1L

    fun numberOfItemForOpenCharacter(characterId: Long) {

    }
}