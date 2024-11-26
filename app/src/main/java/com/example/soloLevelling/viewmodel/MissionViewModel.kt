package com.example.soloLevelling.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.soloLevelling.model.entity.Mission
import com.example.soloLevelling.model.entity.UserMission
import com.example.soloLevelling.model.repository.MissionRepository
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MissionViewModel(private val repository: MissionRepository) : ViewModel() {

    val missions = mutableStateListOf<Mission>()

    init {
        viewModelScope.launch {
            missions.addAll(repository.getAllMissions())
        }
    }

    fun assignMissionsToUser(userId: Int?, mission: Mission) {
        if (userId == null) {
            throw IllegalStateException("Erro: userId não está definido ao criar uma missão.")
        }
        viewModelScope.launch {
            // Adiciona a missão ao banco e à lista global
            repository.insertMissionAndAssignToUser(userId, mission)
            if (missions.none { it.id == mission.id }) {
                missions.add(mission) // Apenas adiciona a missão se ela ainda não estiver na lista
            }
        }
    }



    suspend fun getUserMissions(userId: Int): List<UserMission> {
        return repository.getUserMissions(userId)
    }


    fun deleteMission(mission: Mission) {
        viewModelScope.launch {
            repository.deleteMission(mission)
            repository.deleteUserMissionById(missionId = mission.id)
            missions.remove(mission)
        }
    }

    fun updateMission(mission: Mission) {
        viewModelScope.launch {
            repository.updateMission(mission)
            val index = missions.indexOfFirst { it.id == mission.id }
            if (index != -1) {
                missions[index] = mission
            }
        }
    }

}

