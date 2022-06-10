package com.example.membercharacter.domain

import org.jetbrains.exposed.dao.id.LongIdTable

object Characters : LongIdTable("characters") {
    val name = varchar("name", 100).uniqueIndex()
    val health = integer("health").default(3)
    val stamina = integer("stamina").default(10)
}