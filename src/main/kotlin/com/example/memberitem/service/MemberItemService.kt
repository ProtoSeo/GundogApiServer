package com.example.memberitem.service

import com.example.memberitem.dto.MemberItemRequest
import com.example.memberitem.dto.MemberItemResponse
import com.example.memberitem.repository.MemberItemRepository
import io.ktor.server.auth.jwt.*

class MemberItemService(private val memberItemRepository: MemberItemRepository) {

    fun updateMemberItems(
        principal: JWTPrincipal,
        memberItemRequests: List<MemberItemRequest>,
    ): List<MemberItemResponse> {
        val memberId = principal.payload.getClaim("id").asLong()
        memberItemRequests.forEach { request -> memberItemRepository.update(memberId, request) }
        return memberItemRepository.findAllByMemberId(memberId)
    }

    fun getMemberItems(principal: JWTPrincipal): List<MemberItemResponse> {
        val memberId = principal.payload.getClaim("id").asLong()
        return memberItemRepository.findAllByMemberId(memberId)
    }

}