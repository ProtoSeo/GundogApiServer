package com.example.common.exception

data class ExceptionMessage(val statusCode: String, val errorMsg: String) {
    companion object {
        fun of(type: ExceptionType) = ExceptionMessage(type.getHttpStatus().description, type.getErrorMessage())
    }
}
