package com.example.member.service

import com.example.member.dto.MemberLoginResponse
import com.example.member.dto.MemberRequest
import com.example.member.exception.MemberException
import com.example.member.exception.MemberExceptionType
import com.example.member.repository.MemberRepository
import com.example.utils.JwtProvider
import java.util.*

class MemberService(private val memberRepository: MemberRepository) {
    private val base64Encoder = Base64.getEncoder()

    fun register(request: MemberRequest): Long {
        if(isDuplicateEmail(request.email)) {
            throw MemberException(MemberExceptionType.DUPLICATE_EMAIL)
        }
        return memberRepository.save(request.copy(password = String(base64Encoder.encode(request.password.toByteArray()))))
    }

    fun login(request: MemberRequest): MemberLoginResponse {
        val member = memberRepository.findByEmail(request.email)
        if(member.password == String(base64Encoder.encode(request.password.toByteArray()))) {
            return MemberLoginResponse(member.email, JwtProvider.createJWT(member))
        }
        throw MemberException(MemberExceptionType.UN_AUTHORIZATION)
    }

    fun isDuplicateEmail(email: String) = memberRepository.existsMemberByEmail(email)
}