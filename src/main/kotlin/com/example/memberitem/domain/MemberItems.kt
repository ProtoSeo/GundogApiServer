package com.example.memberitem.domain

import com.example.member.domain.Members
import org.jetbrains.exposed.dao.id.LongIdTable

object MemberItems : LongIdTable(name = "member_item") {
    val memberId = reference("member_id", Members)
    val itemId = reference("item_id", Items)
    val count = long("count").default(0)
}