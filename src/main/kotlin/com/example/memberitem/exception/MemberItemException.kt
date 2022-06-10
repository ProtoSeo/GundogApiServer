package com.example.memberitem.exception

import com.example.common.exception.BaseException
import com.example.common.exception.ExceptionType

class MemberItemException(private val exceptionType: ExceptionType) : BaseException() {
    override fun getExceptionType(): ExceptionType {
        return this.exceptionType
    }
}