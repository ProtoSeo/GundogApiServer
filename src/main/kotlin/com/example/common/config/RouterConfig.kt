package com.example.common.config

import com.example.member.memberRoute
import com.example.member.service.MemberService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.kodein.di.instance

fun Application.configureRouting() {
    val memberService: MemberService by ModuleConfig.kodein.instance()

    routing {
        memberRoute(memberService)
    }
}