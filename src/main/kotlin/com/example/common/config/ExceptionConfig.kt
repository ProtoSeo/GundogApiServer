package com.example.common.config

import com.example.common.exception.BaseException
import com.example.common.exception.ExceptionMessage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureException() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            if (cause is BaseException) {
                val statusCode = cause.getExceptionType().getHttpStatus()
                call.respond(statusCode, ExceptionMessage.of(cause.getExceptionType()))
            } else if (cause is ParameterConversionException) {
                call.respond(HttpStatusCode.BadRequest,
                    ExceptionMessage(HttpStatusCode.BadRequest.description, "요청 파라미터를 확인해주세요."))
            }
        }
    }
}