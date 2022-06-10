package com.example.memberstage.exception

import com.example.common.exception.BaseException
import com.example.common.exception.ExceptionType

class MemberStageException(private val exceptionType: ExceptionType) : BaseException() {

    override fun getExceptionType(): ExceptionType {
        return exceptionType
    }
}