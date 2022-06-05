package com.example.membercharacter.exception

import com.example.common.exception.BaseException
import com.example.common.exception.ExceptionType

class MemberCharacterException(private val exceptionType: ExceptionType) : BaseException() {

    override fun getExceptionType(): ExceptionType {
        return this.exceptionType
    }
}