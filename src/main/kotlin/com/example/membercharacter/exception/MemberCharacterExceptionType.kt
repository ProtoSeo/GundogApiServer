package com.example.membercharacter.exception

import com.example.common.exception.ExceptionType
import io.ktor.http.*

enum class MemberCharacterExceptionType(private val httpStatusCode: HttpStatusCode, private val errorMsg: String) :
    ExceptionType {
    NOT_FOUND(HttpStatusCode.NotFound, "해당 캐릭터에 대한 정보를 찾을 수 없습니다.");

    override fun getHttpStatus(): HttpStatusCode {
        return this.httpStatusCode
    }

    override fun getErrorMessage(): String {
        return this.errorMsg
    }
}