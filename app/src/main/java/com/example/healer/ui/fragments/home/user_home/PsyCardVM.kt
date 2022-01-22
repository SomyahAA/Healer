package com.example.healer.ui.fragments.home.user_home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.healer.models.Appointment
import com.example.healer.repository.Repository

class PsyCardVM : ViewModel() {

    private val repo = Repository.getInstance()

    fun getAppointments(): LiveData<MutableList<Appointment>>{
        val liveDataList = liveData {
            emit(repo.getAppointments())
        }
        return liveDataList
    }

}