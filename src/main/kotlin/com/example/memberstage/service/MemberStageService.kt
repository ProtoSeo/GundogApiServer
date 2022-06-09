package com.example.memberstage.service

import com.example.memberstage.dto.StageRankInfo
import com.example.memberstage.dto.MemberStageRequest
import com.example.memberstage.dto.StageInfo
import com.example.memberstage.exception.MemberStageException
import com.example.memberstage.exception.MemberStageExceptionType.*
import com.example.memberstage.repository.MemberStageRepository
import io.ktor.server.auth.jwt.*

class MemberStageService(private val memberStageRepository: MemberStageRepository) {

    fun updateMemberStageResult(principal: JWTPrincipal, request: MemberStageRequest): StageInfo {
        val memberId = principal.payload.getClaim("id").asLong()
        return memberStageRepository.update(memberId, request)
            ?: throw MemberStageException(MEMBER_STAGE_NOT_FOUND)
    }

    fun getRankingToStage(stageId: Long): List<StageRankInfo> {
        return memberStageRepository.findRankingByStageId(stageId)
    }

    fun getMemberStage(stageId: Long, memberId: Long): StageInfo {
        return memberStageRepository.findByMemberIdAndStageId(memberId, stageId)
            ?: throw MemberStageException(MEMBER_STAGE_NOT_FOUND)
    }

    fun getStages(principal: JWTPrincipal): List<StageInfo> {
        val memberId = principal.payload.getClaim("id").asLong()
        return memberStageRepository.findAllByMemberId(memberId)
    }
}