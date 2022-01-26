package com.example.healer.ui.fragments.home.psychologist_home

import androidx.lifecycle.*
import com.example.healer.models.Appointment
import com.example.healer.repository.Repository
import kotlinx.coroutines.launch
import java.util.*

class SetAppointmentViewModel : ViewModel() {

    private val repo = Repository.getInstance()

    fun getPsychologistAppointments(): LiveData<MutableList<Appointment>> {
        val liveDataList = liveData {
            emit(repo.getPsychologistAppointments())
        }
        return liveDataList
    }

    fun addAppointment(appointment: Appointment) {
        viewModelScope.launch {
            repo.addAppointment(appointment)
        }
    }

    suspend fun appointmentAlreadyExist(appointment: Appointment): Boolean {
        return repo.appointmentAlreadyExist(appointment)
    }

    suspend fun deleteAppointment(deletedAppointment: Int) {
        return repo.deleteAppointment(deletedAppointment)
    }

}