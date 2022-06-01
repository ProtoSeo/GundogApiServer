package com.example.common.config

import com.example.member.router.memberRoute
import com.example.member.service.MemberService
import com.example.memberstage.router.memberStageRoute
import com.example.memberstage.service.MemberStageService
import com.example.stage.router.stageRouter
import com.example.stage.service.StageService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.kodein.di.instance

fun Application.configureRouting() {
    val memberService: MemberService by ModuleConfig.kodein.instance()
    val stageService: StageService by ModuleConfig.kodein.instance()
    val memberStageService: MemberStageService by ModuleConfig.kodein.instance()

    routing {
        memberRoute(memberService)
        memberStageRoute(memberStageService)
        stageRouter(stageService)
    }
}