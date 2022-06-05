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

    fun save(memberId: Long, request: MemberStageRequest): Boolean {
        transaction {
            MemberStages.insert { row ->
                row[this.memberId] = memberId
                row[stageId] = request.stageId
                row[isClear] = request.isClear
                row[bestScore] = request.bestScore
            }
        }
        return true
    }

    fun update(memberId: Long, request: MemberStageRequest): Boolean {
        return transaction {
            val memberStage = MemberStages.select {
                MemberStages.memberId eq memberId and (MemberStages.stageId eq request.stageId)
            }.firstOrNull()

            val isClear = memberStage?.get(MemberStages.isClear) ?: false
            val bestScore = memberStage?.get(MemberStages.bestScore) ?: 0L

            if ((isClear == request.isClear && bestScore < request.bestScore) || (!isClear && request.isClear)) {
                MemberStages.update({ MemberStages.memberId eq memberId and (MemberStages.stageId eq request.stageId) }) { row ->
                    row[this.isClear] = request.isClear
                    row[this.bestScore] = request.bestScore
                }
                true
            } else {
                false
            }
        }
    }

    fun findByMemberIdAndStageId(memberId: Long, stageId: Long): MemberStageInfo? {
        return transaction {
            (MemberStages.innerJoin(Members))
                .slice(MemberStages.id, Members.email, MemberStages.bestScore, MemberStages.isClear)
                .select { MemberStages.memberId eq memberId and (MemberStages.stageId eq stageId) }
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

    fun existsMemberStageByMemberIdAndStageId(memberId: Long, stageId: Long): Boolean {
        return transaction {
            MemberStages.slice(MemberStages.id)
                .select { MemberStages.memberId eq memberId and (MemberStages.stageId eq stageId) }.count() > 0
        }
    }
}