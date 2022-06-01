package com.example.memberstage.service

import com.example.memberstage.dto.MemberStageRankResponse
import com.example.memberstage.dto.MemberStageRequest
import com.example.memberstage.dto.MemberStageResponse
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

    fun getRankingToStage(stageId: Long): List<MemberStageRankResponse> {
        return memberStageRepository.findRankingByStageId(stageId)
    }

    fun getMemberStage(stageId: Long, memberId: Long): MemberStageResponse {
        return memberStageRepository.findByMemberIdAndStageId(memberId, stageId)
    }
}