package com.example.stage.service

import com.example.stage.repository.StageRepository

class StageService(private val stageRepository: StageRepository) {

    fun getAllStage() = stageRepository.findAll()

}