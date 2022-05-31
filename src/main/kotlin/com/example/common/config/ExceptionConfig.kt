package com.example.common.config

import com.example.common.exception.ExceptionMessage
import com.example.member.exception.MemberException
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureException() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            when(cause) {
                is MemberException -> {
                    val statusCode = cause.getExceptionType().getHttpStatus()
                    call.respond(statusCode, ExceptionMessage.of(cause.getExceptionType()))
                }
            }
        }
    }
}