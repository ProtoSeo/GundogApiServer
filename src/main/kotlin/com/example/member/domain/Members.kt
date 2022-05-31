package com.example.member.domain

import org.jetbrains.exposed.dao.id.LongIdTable

object Members : LongIdTable(name = "member") {
    val email = varchar("email", length = 50).uniqueIndex()
    val password = varchar("password", length = 100)
}