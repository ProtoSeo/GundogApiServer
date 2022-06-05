package com.example.memberstage.exception

import com.example.common.exception.ExceptionType
import io.ktor.http.*

enum class MemberStageExceptionType(private val httpStatusCode: HttpStatusCode, private val errorMsg: String) :
    ExceptionType {
    MEMBER_STAGE_NOT_FOUND(HttpStatusCode.NotFound, "해당 스테이지의 게임 정보가 존재하지 않습니다.");

    override fun getHttpStatus(): HttpStatusCode {
        return this.httpStatusCode
    }

    override fun getErrorMessage(): String {
        return this.errorMsg
    }
}