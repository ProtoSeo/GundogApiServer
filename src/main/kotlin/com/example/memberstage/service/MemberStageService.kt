package com.example.memberstage.service

import com.example.memberstage.dto.StageRankInfo
import com.example.memberstage.dto.MemberStageRequest
import com.example.memberstage.dto.MemberStageInfo
import com.example.memberstage.exception.MemberStageException
import com.example.memberstage.exception.MemberStageExceptionType.*
import com.example.memberstage.repository.MemberStageRepository
import io.ktor.server.auth.jwt.*

class MemberStageService(private val memberStageRepository: MemberStageRepository) {

    fun saveOrUpdateMemberStageResult(principal: JWTPrincipal, request: MemberStageRequest): String {
        val memberId = principal.payload.getClaim("id").asLong()
        val isUpdate = if (memberStageRepository.existsMemberStageByMemberIdAndStageId(memberId, request.stageId)) {
            memberStageRepository.update(memberId, request)
        } else {
            memberStageRepository.save(memberId, request)
        }
        return if (isUpdate) "성공적으로 업데이트되었습니다." else "기존 점수가 높으므로 업데이트 되지 않습니다."
    }

    fun getRankingToStage(stageId: Long): List<StageRankInfo> {
        return memberStageRepository.findRankingByStageId(stageId)
    }

    fun getMemberStage(stageId: Long, memberId: Long): MemberStageInfo {
        return memberStageRepository.findByMemberIdAndStageId(memberId, stageId)
            ?: throw MemberStageException(MEMBER_STAGE_NOT_FOUND)
    }
}