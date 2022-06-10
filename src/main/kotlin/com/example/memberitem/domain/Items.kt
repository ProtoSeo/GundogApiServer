package com.example.memberitem.domain

import org.jetbrains.exposed.dao.id.LongIdTable

object Items : LongIdTable(name = "item") {
    val name = varchar(name = "name", length = 255)
    val description = text("description")
    val limit = long(name = "limit").default(Long.MAX_VALUE)
}