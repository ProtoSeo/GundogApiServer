package com.example.memberitem.service

import com.example.memberitem.dto.MemberItemRequest
import com.example.memberitem.dto.MemberItemResponse
import com.example.memberitem.repository.MemberItemRepository
import io.ktor.server.auth.jwt.*

class MemberItemService(private val memberItemRepository: MemberItemRepository) {

    fun updateMemberItems(principal: JWTPrincipal, memberItemRequests: List<MemberItemRequest>) {
        val memberId = principal.payload.getClaim("id").asLong()
        for (request in memberItemRequests) {
            memberItemRepository.update(memberId, request)
        }
    }

    fun getMemberItems(principal: JWTPrincipal): List<MemberItemResponse> {
        val memberId = principal.payload.getClaim("id").asLong()
        return memberItemRepository.findAllByMemberId(memberId)
    }

}