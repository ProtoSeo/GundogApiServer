package com.example.common.config

import com.example.utils.JwtProvider
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity() {

    authentication {
        jwt {
            val jwtAudience = this@configureSecurity.environment.config.property("jwt.audience").getString()
            val jwtIssuer = this@configureSecurity.environment.config.property("jwt.issuer").getString()
            realm = this@configureSecurity.environment.config.property("jwt.realm").getString()
            verifier(JwtProvider.verifier)
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience) && credential.payload.issuer.contains(jwtIssuer))
                    JWTPrincipal(credential.payload)
                else
                    null
            }
        }
    }
}