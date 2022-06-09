package com.example.memberstage.repository

import com.example.member.domain.Members
import com.example.memberstage.domain.MemberStages
import com.example.memberstage.domain.Stages
import com.example.memberstage.dto.MemberStageRequest
import com.example.memberstage.dto.StageRankInfo
import com.example.memberstage.dto.StageInfo
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class MemberStageRepository {
    init {
        transaction {
            SchemaUtils.create(MemberStages)
            SchemaUtils.create(Stages)
        }
    }

    fun saveMemberStages(memberId: Long) {
        transaction {
            val stageIds = Stages.slice(Stages.id).selectAll().orderBy(Stages.id).map { it[Stages.id].value }.toList()
            var first = true
            MemberStages.batchInsert(stageIds, shouldReturnGeneratedValues = false) { stageId ->
                this[MemberStages.memberId] = memberId
                this[MemberStages.stageId] = stageId
                if (first) {
                    this[MemberStages.isOpen] = first
                    first = false
                }
            }
        }
    }

    fun update(memberId: Long, request: MemberStageRequest): StageInfo? {
        transaction {
            val memberStage = MemberStages.innerJoin(Stages)
                .slice(MemberStages.id, MemberStages.isClear, MemberStages.bestScore, Stages.nextId)
                .select {
                    MemberStages.memberId eq memberId and (MemberStages.stageId eq request.stageId)
                }.limit(1).first()

            if (request.isClear && memberStage[MemberStages.bestScore] < request.bestScore) {
                MemberStages.update({ MemberStages.id eq memberStage[MemberStages.id].value }) { row ->
                    row[isClear] = true
                    row[bestScore] = request.bestScore
                }
            }

            if (!memberStage[MemberStages.isClear] && request.isClear && memberStage[Stages.nextId] != -1L) {
                MemberStages.update(
                    { MemberStages.memberId eq memberId and (MemberStages.stageId eq memberStage[Stages.nextId]) }
                ) { row ->
                    row[isOpen] = true
                }
            }
        }
        return findByMemberIdAndStageId(memberId, request.stageId)
    }

    fun findByMemberIdAndStageId(memberId: Long, stageId: Long): StageInfo? {
        return transaction {
            MemberStages.slice(
                MemberStages.id,
                Members.email,
                MemberStages.bestScore,
                MemberStages.isClear,
                MemberStages.isOpen)
                .select { MemberStages.memberId eq memberId and (MemberStages.stageId eq stageId) }
                .limit(1)
                .map {
                    StageInfo(
                        it[MemberStages.id].value,
                        it[MemberStages.bestScore],
                        it[MemberStages.isClear],
                        it[MemberStages.isOpen]
                    )
                }
                .firstOrNull()
        }
    }

    fun findAllByMemberId(memberId: Long): List<StageInfo> {
        return transaction {
            MemberStages.slice(
                MemberStages.id,
                Members.email,
                MemberStages.bestScore,
                MemberStages.isClear,
                MemberStages.isOpen)
                .select { MemberStages.memberId eq memberId }
                .map {
                    StageInfo(
                        it[MemberStages.id].value,
                        it[MemberStages.bestScore],
                        it[MemberStages.isClear],
                        it[MemberStages.isOpen]
                    )
                }.toList()
        }
    }

    fun findRankingByStageId(stageId: Long): List<StageRankInfo> {
        return transaction {
            (MemberStages.join(Members, JoinType.INNER, additionalConstraint = { MemberStages.memberId eq Members.id }))
                .slice(Members.email, MemberStages.bestScore)
                .select { MemberStages.stageId eq stageId and (MemberStages.isClear eq true) }
                .orderBy(MemberStages.bestScore, SortOrder.DESC)
                .limit(5)
                .mapIndexed { index, row -> StageRankInfo(index + 1, row[Members.email], row[MemberStages.bestScore]) }
                .toList()
        }
    }
}