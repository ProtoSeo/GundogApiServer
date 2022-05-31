package com.example.common.config

import com.example.member.repository.MemberRepository
import com.example.member.service.MemberService
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

object ModuleConfig {
    private val memberModule = DI.Module("Member") {
        bindSingleton { MemberService(instance()) }
        bindSingleton { MemberRepository() }
    }

    internal val kodein = DI {
        import(memberModule)
    }
}