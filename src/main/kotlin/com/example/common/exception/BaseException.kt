package com.example.common.exception

abstract class BaseException : RuntimeException() {
    abstract fun getExceptionType(): ExceptionType
}