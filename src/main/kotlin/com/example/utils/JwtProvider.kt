package com.example.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.example.member.domain.Member
import java.util.*

object JwtProvider {
    private const val validityInMs = 36_000_00 * 10 // 10 hours
    private const val issuer = "GunDog-Server"
    private const val audience = "GunDog-Game"
    private const val secret = "GunDog-secret"

    val verifier: JWTVerifier = JWT
        .require(Algorithm.HMAC256(secret))
        .withAudience(audience)
        .withIssuer(issuer)
        .build()

    fun decodeJWT(token: String): DecodedJWT = JWT.require(Algorithm.HMAC256(secret)).build().verify(token)

    fun createJWT(member: Member): String =
        JWT.create()
            .withIssuedAt(Date())
            .withSubject("Authentication")
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("email", member.email)
            .withClaim("id", member.id)
            .withExpiresAt(Date(System.currentTimeMillis() + validityInMs)).sign(Algorithm.HMAC256(secret))
}