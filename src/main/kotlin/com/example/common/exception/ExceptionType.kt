package com.example.common.exception

import io.ktor.http.*

interface ExceptionType {
    fun getHttpStatus(): HttpStatusCode
    fun getErrorMessage(): String
}