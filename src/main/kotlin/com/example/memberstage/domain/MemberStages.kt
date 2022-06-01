package com.example.memberstage.domain

import com.example.member.domain.Members
import com.example.stage.domain.Stages
import org.jetbrains.exposed.dao.id.LongIdTable

object MemberStages : LongIdTable(name = "member_stage") {
    val memberId = reference("member_id", Members)
    val stageId = reference("stage_id", Stages)
    val isClear = bool(name = "is_clear").default(false)
    val bestScore = long(name = "best_score").default(0L)
}