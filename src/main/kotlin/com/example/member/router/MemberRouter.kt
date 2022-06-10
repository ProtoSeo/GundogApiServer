package com.example.member.router

import com.example.member.domain.Members.email
import com.example.member.dto.MemberRequest
import com.example.member.exception.MemberException
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
    val regex = Regex("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+\$")
    route("api/v1") {
        post("login") {
            val memberRequest = call.receive<MemberRequest>()
            val loginResponse = memberService.login(memberRequest)
            call.response.cookies.append(
                Cookie(
                    "token",
                    loginResponse.token,
                    httpOnly = true,
                    maxAge = 36_000_00 * 10
                )
            )
            call.respond(loginResponse)
        }
        post("signup") {
            val memberRequest = call.receive<MemberRequest>()
            if (!regex.containsMatchIn(memberRequest.email)) throw MemberException(INVALID_EMAIL)
            val id = memberService.register(memberRequest)
            call.respond(status = HttpStatusCode.Created, id)
        }
        get("members") {
            val email = call.request.queryParameters["email"].toString()
            if (!regex.containsMatchIn(email)) throw MemberException(INVALID_EMAIL)
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