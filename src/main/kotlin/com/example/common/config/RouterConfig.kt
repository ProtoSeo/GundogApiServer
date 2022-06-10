package com.example.common.config

import com.example.member.router.memberRoute
import com.example.member.service.MemberService
import com.example.membercharacter.router.memberCharacterRoute
import com.example.membercharacter.service.MemberCharacterService
import com.example.memberitem.router.memberItemRoute
import com.example.memberitem.service.MemberItemService
import com.example.memberstage.router.memberStageRoute
import com.example.memberstage.service.MemberStageService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.kodein.di.instance

fun Application.configureRouting() {
    val memberService: MemberService by ModuleConfig.kodein.instance()
    val memberStageService: MemberStageService by ModuleConfig.kodein.instance()
    val memberItemService: MemberItemService by ModuleConfig.kodein.instance()
    val memberCharacterService: MemberCharacterService by ModuleConfig.kodein.instance()

    routing {
        memberRoute(memberService)
        memberStageRoute(memberStageService)
        memberItemRoute(memberItemService)
        memberCharacterRoute(memberCharacterService)
    }
}