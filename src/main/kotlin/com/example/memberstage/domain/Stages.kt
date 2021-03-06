package com.example.memberstage.domain

import org.jetbrains.exposed.dao.id.LongIdTable

object Stages : LongIdTable(name = "stage") {
    val name = varchar(name = "name", length = 100).uniqueIndex()
    val level = integer(name = "level").uniqueIndex()
    val nextId = long(name = "next_id").default(-1L)
}