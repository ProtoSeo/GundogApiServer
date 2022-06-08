package com.example.memberstage.repository

import com.example.member.domain.Members
import com.example.memberstage.domain.MemberStages
import com.example.memberstage.dto.MemberStageRequest
import com.example.memberstage.dto.StageRankInfo
import com.example.memberstage.dto.MemberStageInfo
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class MemberStageRepository {
    init {
        transaction {
            SchemaUtils.create(MemberStages)
        }
    }

    fun saveOrUpdate(memberId: Long, request: MemberStageRequest): MemberStageInfo? {
        return transaction {
            val memberStage = MemberStages.slice(MemberStages.isClear, MemberStages.bestScore).select {
                MemberStages.memberId eq memberId and (MemberStages.stageId eq request.stageId)
            }.limit(1).firstOrNull()

            if (memberStage == null) {
                MemberStages.insert { row ->
                    row[this.memberId] = memberId
                    row[stageId] = request.stageId
                    row[isClear] = request.isClear
                    if (request.isClear) row[bestScore] = request.bestScore
                }
            } else {
                if (request.isClear && memberStage[MemberStages.bestScore] < request.bestScore) {
                    MemberStages.update { row ->
                        row[isClear] = true
                        row[bestScore] = request.bestScore
                    }
                }
            }
            findByMemberIdAndStageId(memberId, request.stageId)
        }
    }

    fun findByMemberIdAndStageId(memberId: Long, stageId: Long): MemberStageInfo? {
        return transaction {
            (MemberStages.innerJoin(Members))
                .slice(MemberStages.id, Members.email, MemberStages.bestScore, MemberStages.isClear)
                .select { MemberStages.memberId eq memberId and (MemberStages.stageId eq stageId) }
                .limit(1)
                .map {
                    MemberStageInfo(
                        it[MemberStages.id].value,
                        it[Members.email],
                        it[MemberStages.bestScore],
                        it[MemberStages.isClear]
                    )
                }
                .firstOrNull()
        }
    }

    fun findRankingByStageId(stageId: Long): List<StageRankInfo> {
        return transaction {
            (MemberStages.join(Members, JoinType.INNER, additionalConstraint = { MemberStages.memberId eq Members.id }))
                .slice(MemberStages.id, Members.email, MemberStages.bestScore)
                .select { MemberStages.stageId eq stageId and (MemberStages.isClear eq true) }
                .orderBy(MemberStages.bestScore, SortOrder.DESC)
                .limit(10)
                .map {
                    StageRankInfo(it[MemberStages.id].value, it[Members.email], it[MemberStages.bestScore])
                }.toList()
        }
    }
}