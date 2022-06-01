package com.example.stage.service

import com.example.stage.dto.StageSaveRequest
import com.example.stage.repository.StageRepository

class StageService(private val stageRepository: StageRepository) {

    fun saveStage(request: StageSaveRequest): Long {
        return stageRepository.save(request)
    }

    fun getAllStage() = stageRepository.findAll()

}