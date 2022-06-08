package com.example.membercharacter.repository

import com.example.character.domain.Characters
import com.example.membercharacter.domain.ItemLimit
import com.example.membercharacter.domain.LevelUpValue
import com.example.membercharacter.domain.LevelUpValue.*
import com.example.membercharacter.domain.MemberCharacters
import com.example.membercharacter.dto.MemberCharacter
import com.example.memberitem.domain.MemberItems
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
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

    fun update(memberCharacterId: Long): MemberCharacter? {
        transaction {
            MemberCharacters.update({ MemberCharacters.id eq memberCharacterId }) { row ->
                row[isOpen] = true
            }
        }
        return findById(memberCharacterId)
    }

    fun update(memberId: Long, memberCharacterId: Long, wantedLevel: LevelUpValue): MemberCharacter? {
        transaction {
            val wantedColumn =
                if (wantedLevel == HEALTH) MemberCharacters.healthLevel else MemberCharacters.staminaLevel

            val memberItem = MemberItems.slice(MemberItems.count)
                .select(MemberItems.memberId eq memberId and (MemberItems.itemId eq 1L))
                .limit(1)
                .first()

            val memberCharacter = MemberCharacters.slice(wantedColumn)
                .select { MemberCharacters.id eq memberCharacterId }
                .limit(1)
                .first()

            val memberCharacterValueLevel = memberCharacter[wantedColumn]
            val numberOfNeedsDogGum =
                if (wantedLevel == HEALTH) ItemLimit.numberOfDogGumForHealthLevelUp(memberCharacterValueLevel)
                else ItemLimit.numberOfDogGumForStaminaLevelUp(memberCharacterValueLevel)

            val numberOfUserHasDogGum = memberItem[MemberItems.count]

            if (numberOfNeedsDogGum <= numberOfUserHasDogGum) {
                MemberItems.update({ MemberItems.memberId eq memberId and (MemberItems.itemId eq 1L) }) { row ->
                    row[count] = numberOfUserHasDogGum - numberOfNeedsDogGum
                }
                MemberCharacters.update({ MemberCharacters.id eq memberCharacterId }) { row ->
                    row[healthLevel] = memberCharacterValueLevel + 1
                }
            }
        }
        return findById(memberCharacterId)
    }
}