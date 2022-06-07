package com.example.member.router

import com.example.member.dto.MemberRequest
import com.example.member.exception.MemberException
import com.example.member.exception.MemberExceptionType
import com.example.member.exception.MemberExceptionType.*
import com.example.member.service.MemberService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.memberRoute(memberService: MemberService) {
    route("api/v1") {
        post("login") {
            val memberRequest = call.receive<MemberRequest>()
            val login = memberService.login(memberRequest)
            call.respond(login)
        }
        post("signup") {
            val memberRequest = call.receive<MemberRequest>()
            val id = memberService.register(memberRequest)
            call.respond(status = HttpStatusCode.Created, id)
        }
        get("members") {
            val email = call.request.queryParameters["email"].toString()
            if (memberService.isDuplicateEmail(email)) {
                call.respond(HttpStatusCode.Conflict)
            } else {
                call.respond(HttpStatusCode.NoContent)
            }
        }
        authenticate {
            get("members/auth") {
                val principal = call.principal<JWTPrincipal>() ?: throw MemberException(UN_AUTHORIZED)
                val username = principal.payload.getClaim("email").asString()
                val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                call.respondText("Hello, $username! Token is expired at $expiresAt ms.")
            }
        }
    }
}