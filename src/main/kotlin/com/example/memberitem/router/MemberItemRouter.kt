package com.example.memberitem.router

import com.example.memberitem.dto.MemberItemRequest
import com.example.memberitem.service.MemberItemService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.memberItemRoute(memberItemService: MemberItemService) {
    route("api/v1") {
        authenticate {
            get("items") {
                val principal = call.principal<JWTPrincipal>()
                val memberItems = memberItemService.getMemberItems(principal!!)
                call.respond(memberItems)
            }
            post("members/{memberId}/items") {
                val principal = call.principal<JWTPrincipal>()
                val memberItemRequests = call.receive<List<MemberItemRequest>>()
                memberItemService.updateMemberItems(principal!!, memberItemRequests)
                call.respond("success!!")
            }
        }
    }
}