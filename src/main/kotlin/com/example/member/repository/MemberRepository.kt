package com.example.member.repository

import com.example.member.domain.Member
import com.example.member.domain.Members
import com.example.member.dto.MemberRequest
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class MemberRepository {

    fun existsMemberByEmail(email: String): Boolean {
        return transaction {
            Members.slice(Members.id).select(Members.email eq email).count() > 0
        }
    }

    fun save(request: MemberRequest): Long {
        return transaction {
            Members.insertAndGetId { row ->
                row[email] = request.email
                row[password] = request.password
            }.value
        }
    }

    fun delete(id: Long) {
        transaction {
            Members.deleteWhere { Members.id eq id }
        }
    }

    fun findByEmail(email: String): Member? {
        return transaction {
            Members.select(Members.email eq email).limit(1)
                .map { Member(it[Members.id].value, it[Members.password], it[Members.email]) }
                .firstOrNull()
        }
    }
}