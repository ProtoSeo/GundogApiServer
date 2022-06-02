package com.example.common.config

import com.example.member.repository.MemberRepository
import com.example.member.service.MemberService
import com.example.memberitem.repository.MemberItemRepository
import com.example.memberitem.service.MemberItemService
import com.example.memberstage.repository.MemberStageRepository
import com.example.memberstage.service.MemberStageService
import com.example.stage.repository.StageRepository
import com.example.stage.service.StageService
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

object ModuleConfig {
    private val memberModule = DI.Module("Member") {
        bindSingleton { MemberService(instance(), instance()) }
        bindSingleton { MemberRepository() }
    }

    private val stageModule = DI.Module("Stage") {
        bindSingleton { StageService(instance()) }
        bindSingleton { StageRepository() }
    }

    private val memberStageModule = DI.Module("MemberStage") {
        bindSingleton { MemberStageService(instance()) }
        bindSingleton { MemberStageRepository() }
    }

    private val memberItemModule = DI.Module("MemberItem") {
        bindSingleton { MemberItemService(instance()) }
        bindSingleton { MemberItemRepository() }
    }

    internal val kodein = DI {
        import(memberModule)
        import(stageModule)
        import(memberStageModule)
        import(memberItemModule)
    }
}