package com.example.member.exception

import com.example.common.exception.BaseException
import com.example.common.exception.ExceptionType

class MemberException(private val exceptionType: MemberExceptionType): BaseException() {

    override fun getExceptionType(): ExceptionType {
        return this.exceptionType
    }

}