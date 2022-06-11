package com.example.memberstage.dto

data class StageDetailDto(
    val memberStageId: Long,
    val stageId: Long,
    val bestScore: Long,
    val isClear: Boolean,
    val isOpen: Boolean,
)