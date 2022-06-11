package com.example.membercharacter.domain

import com.example.membercharacter.domain.LevelLimit.*
import com.example.membercharacter.exception.MemberCharacterException
import com.example.membercharacter.exception.MemberCharacterExceptionType.*

enum class LevelLimit(val limit: Int) {
    HEALTH_LEVEL(6), STAMINA_LEVEL(7);
}

enum class LevelUpValue {
    STAMINA, HEALTH
}

object ItemLimit {
    private val healthLevelUpWantDogGum = longArrayOf(3, 6, 9, 18, 27, 27, 27)
    private val staminaLevelUpWantDogGum = longArrayOf(3, 6, 9, 18, 27, 27, 27, 27)
    private val characterUnlockNeedItemCount = longArrayOf(0, 0, 4, 4, 4, 4)
    private val characterUnlockNeedItemId = mapOf(1L to intArrayOf(), 2L to intArrayOf(2, 3), 3L to intArrayOf(4, 5))

    fun numberOfDogGumForHealthLevelUp(nowLevel: Int) =
        if (nowLevel <= HEALTH_LEVEL.limit) healthLevelUpWantDogGum[nowLevel] else -1L

    fun numberOfDogGumForStaminaLevelUp(nowLevel: Int) =
        if (nowLevel <= STAMINA_LEVEL.limit) staminaLevelUpWantDogGum[nowLevel] else -1L

    fun numberOfItemForUnlockCharacter(characterId: Long): List<UnlockItem> {
        val itemIds: IntArray = characterUnlockNeedItemId[characterId] ?: throw MemberCharacterException(BAD_REQUEST)
        return itemIds.map { UnlockItem(it.toLong(), characterUnlockNeedItemCount[it]) }
    }
}

data class UnlockItem(val id: Long, val count: Long)