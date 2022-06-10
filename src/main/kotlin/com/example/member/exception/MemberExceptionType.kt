package com.example.member.exception

import com.example.common.exception.ExceptionType
import io.ktor.http.*

enum class MemberExceptionType(private val httpStatusCode: HttpStatusCode, private val errorMsg: String) : ExceptionType {
    DUPLICATE_EMAIL(HttpStatusCode.Conflict, "이메일이 중복되었습니다."),
    NOT_FOUND(HttpStatusCode.NotFound, "해당 회원이 존재하지 않습니다."),
    BAD_REQUEST(HttpStatusCode.BadRequest, "이메일 혹은 비밀번호가 잘못 입력되었습니다."),
    UN_AUTHORIZED(HttpStatusCode.Unauthorized, "토큰이 유효하지 않습니다."),
    INVALID_EMAIL(HttpStatusCode.BadRequest, "이메일 형식을 확인해주세요.");

    override fun getHttpStatus(): HttpStatusCode {
        return this.httpStatusCode
    }

    override fun getErrorMessage(): String {
        return this.errorMsg
    }
}