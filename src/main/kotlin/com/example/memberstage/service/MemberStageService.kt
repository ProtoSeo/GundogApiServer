package com.example.memberstage.service

import com.example.memberstage.dto.StageRankDto
import com.example.memberstage.dto.StageClearRequestDto
import com.example.memberstage.dto.StageDetailDto
import com.example.memberstage.dto.StageDto
import com.example.memberstage.exception.MemberStageException
import com.example.memberstage.exception.MemberStageExceptionType.*
import com.example.memberstage.repository.MemberStageRepository
import io.ktor.server.auth.jwt.*

class MemberStageService(private val memberStageRepository: MemberStageRepository) {

    fun updateMemberStageResult(principal: JWTPrincipal, request: StageClearRequestDto): StageDetailDto {
        val memberId = principal.payload.getClaim("id").asLong()
        return memberStageRepository.update(memberId, request)
            ?: throw MemberStageException(MEMBER_STAGE_NOT_FOUND)
    }

    fun getRankingToStage(stageId: Long): List<StageRankDto> {
        return memberStageRepository.findRankingByStageId(stageId)
    }

    fun getMemberStage(principal: JWTPrincipal, stageId: Long): StageDetailDto {
        val memberId = principal.payload.getClaim("id").asLong()
        return memberStageRepository.findByMemberIdAndStageId(memberId, stageId)
            ?: throw MemberStageException(MEMBER_STAGE_NOT_FOUND)
    }

    fun getStages(principal: JWTPrincipal): List<StageDto> {
        val memberId = principal.payload.getClaim("id").asLong()
        return memberStageRepository.findAllByMemberId(memberId)
    }
}