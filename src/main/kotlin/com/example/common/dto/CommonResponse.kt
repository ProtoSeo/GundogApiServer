package com.example.common.dto

data class CommonResponse<T>(val status: Status, val data: T)

enum class Status {
    SUCCESS, FAIL;
}
