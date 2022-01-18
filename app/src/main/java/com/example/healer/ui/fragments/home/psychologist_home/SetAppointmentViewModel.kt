package com.example.healer.ui.fragments.home.psychologist_home

import androidx.lifecycle.*
import com.example.healer.models.Appointment
import com.example.healer.repository.Repository
import kotlinx.coroutines.launch

class SetAppointmentViewModel : ViewModel() {

    private val repo = Repository.getInstance()

     fun getPsychologistAppointments() : LiveData<MutableList<Appointment>>{
         val liveDataList = liveData {
             emit(repo.getAppointments())
         }
         return liveDataList
    }

    fun addAppointment(appointment: Appointment){
        viewModelScope.launch {
            repo.addAppointment(appointment)
        }
    }
}