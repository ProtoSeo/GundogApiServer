package com.example.membercharacter.repository

import com.example.character.domain.Characters
import com.example.membercharacter.domain.LevelLimit.*
import com.example.membercharacter.domain.MemberCharacters
import com.example.membercharacter.dto.MemberCharacterLevelUpRequest
import com.example.membercharacter.dto.MemberCharacter
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class MemberCharacterRepository {
    init {
        transaction {
            SchemaUtils.create(Characters)
            SchemaUtils.create(MemberCharacters)
        }
    }

    fun saveMemberCharacters(memberId: Long) {
        transaction {
            val characterIds = Characters.slice(Characters.id).selectAll().orderBy(Characters.id)
                .map { it[Characters.id].value }
                .toList()
            var first = true
            MemberCharacters.batchInsert(characterIds, shouldReturnGeneratedValues = false) { characterId ->
                this[MemberCharacters.memberId] = memberId
                this[MemberCharacters.characterId] = characterId
                if (first) {
                    this[MemberCharacters.isOpen] = true
                    first = false
                }
            }
        }
    }

    fun findById(memberCharacterId: Long): MemberCharacter? {
        return transaction {
            MemberCharacters.select { MemberCharacters.id eq memberCharacterId }
                .limit(1)
                .map { row ->
                    MemberCharacter(
                        row[MemberCharacters.id].value,
                        row[MemberCharacters.characterId].value,
                        row[MemberCharacters.healthLevel],
                        row[MemberCharacters.staminaLevel],
                        row[MemberCharacters.isOpen]
                    )
                }.firstOrNull()
        }
    }

    fun findAllByMemberId(memberId: Long): List<MemberCharacter> {
        return transaction {
            MemberCharacters.select { MemberCharacters.memberId eq memberId }
                .orderBy(MemberCharacters.characterId)
                .map { row ->
                    MemberCharacter(
                        row[MemberCharacters.id].value,
                        row[MemberCharacters.characterId].value,
                        row[MemberCharacters.healthLevel],
                        row[MemberCharacters.staminaLevel],
                        row[MemberCharacters.isOpen]
                    )
                }.toList()
        }
    }

    fun update(memberCharacterId: Long, request: MemberCharacterLevelUpRequest): MemberCharacter? {
        transaction {
            MemberCharacters.update({ MemberCharacters.id eq memberCharacterId }) { row ->
                if (request.healthLevel <= HEALTH_LEVEL.limit) row[healthLevel] = request.healthLevel
                if (request.staminaLevel <= STAMINA_LEVEL.limit) row[staminaLevel] = request.staminaLevel
            }
        }
        return findById(memberCharacterId)
    }

    fun update(memberCharacterId: Long): MemberCharacter? {
        transaction {
            MemberCharacters.update({ MemberCharacters.id eq memberCharacterId }) { row ->
                row[isOpen] = true
            }
        }
        return findById(memberCharacterId)
    }
}