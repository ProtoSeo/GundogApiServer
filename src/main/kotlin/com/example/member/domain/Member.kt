package com.example.member.domain

import io.ktor.server.auth.*

data class Member(val id: Long, val password: String, val email: String) : Principal
