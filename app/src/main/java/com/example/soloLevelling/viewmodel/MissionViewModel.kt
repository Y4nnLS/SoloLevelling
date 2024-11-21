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

    fun assignMissionsToUser(userId: Int) {
        viewModelScope.launch {
            repository.assignMissionsToUser(userId)
        }
    }

    suspend fun getUserMissions(userId: Int): List<UserMission> {
        return repository.getUserMissions(userId)
    }
    fun addMission(mission: Mission) {
        viewModelScope.launch {
            repository.insertMission(mission)
            missions.add(mission)
        }
    }

    fun deleteMission(mission: Mission) {
        viewModelScope.launch {
            repository.deleteMission(mission)
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

