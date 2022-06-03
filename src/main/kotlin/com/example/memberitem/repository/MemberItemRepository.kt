package com.example.memberitem.repository

import com.example.item.domain.Items
import com.example.memberitem.domain.MemberItems
import com.example.memberitem.dto.MemberItemRequest
import com.example.memberitem.dto.MemberItemResponse
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class MemberItemRepository {
    init {
        transaction {
            SchemaUtils.create(Items)
            SchemaUtils.create(MemberItems)
        }
    }

    fun saveMemberItems(memberId: Long) {
        transaction {
            val list = Items.selectAll().map { it[Items.id].value }.toList()
            MemberItems.batchInsert(list, shouldReturnGeneratedValues = false) { itemId ->
                this[MemberItems.itemId] = itemId
                this[MemberItems.memberId] = memberId
                this[MemberItems.count] = 0
            }
        }
    }

    fun update(memberId: Long, request: MemberItemRequest) {
        transaction {
            val memberItem = MemberItems.innerJoin(Items).slice(Items.id, Items.limit, MemberItems.count)
                .select(MemberItems.memberId eq memberId and (MemberItems.itemId eq request.itemId)).firstOrNull()

            memberItem?.let {
                MemberItems.update({ MemberItems.memberId eq memberId and (MemberItems.itemId eq request.itemId) }) { row ->
                    row[count] =
                        if (it[Items.limit] >= it[count] + request.addCount) it[count] + request.addCount
                        else it[Items.limit]
                }
            }
        }
    }

    fun findAllByMemberId(memberId: Long): List<MemberItemResponse> {
        return transaction {
            (MemberItems.innerJoin(Items)).slice(Items.id, Items.name, Items.description, MemberItems.count)
                .select { MemberItems.memberId eq memberId }
                .map {
                    MemberItemResponse(it[Items.id].value, it[Items.name], it[Items.description], it[MemberItems.count])
                }.toList()
        }
    }
}