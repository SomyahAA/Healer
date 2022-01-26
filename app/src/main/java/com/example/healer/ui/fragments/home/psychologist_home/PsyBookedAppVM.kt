package com.example.healer.ui.fragments.home.psychologist_home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.healer.models.Appointment
import com.example.healer.repository.Repository

class PsyBookedAppVM : ViewModel() {

    private val repo = Repository.getInstance()

    suspend fun getPsyBookedAppList(): LiveData<List<Appointment>> {
        val liveDataList = liveData {
            emit(repo.getPsyBookedAppList())
        }
        return liveDataList
    }
}