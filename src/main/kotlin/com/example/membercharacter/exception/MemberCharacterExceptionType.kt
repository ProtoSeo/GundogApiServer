package com.example.membercharacter.exception

import com.example.common.exception.ExceptionType
import io.ktor.http.*

enum class MemberCharacterExceptionType(private val httpStatusCode: HttpStatusCode, private val errorMsg: String) :
    ExceptionType {
    NOT_FOUND(HttpStatusCode.NotFound, "해당 캐릭터에 대한 정보를 찾을 수 없습니다."),
    DUPLICATE_UNLOCK(HttpStatusCode.Conflict, "해당 캐릭터는 이미 잠금해제 되었습니다."),
    INSUFFICIENT_ITEM(HttpStatusCode.BadRequest, "아이템 개수가 부족합니다."),
    BAD_REQUEST(HttpStatusCode.BadRequest, "잘못된 요청입니다.");

    override fun getHttpStatus(): HttpStatusCode {
        return this.httpStatusCode
    }

    override fun getErrorMessage(): String {
        return this.errorMsg
    }
}