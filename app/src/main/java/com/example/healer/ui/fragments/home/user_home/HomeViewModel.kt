package com.example.healer.ui.fragments.home.user_home

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.healer.models.Appointment
import com.example.healer.models.Psychologist
import com.example.healer.repository.Repository

private const val TAG = "HomeViewModel"

class HomeViewModel : ViewModel() {

    private val repo = Repository.getInstance()

    fun psyLiveData(): LiveData<List<Psychologist>> {
        val liveDataList = liveData {
            emit(repo.getAllPsychologist())
            Log.d(TAG, "from repo ${repo.getAllPsychologist()}")
        }
        return liveDataList
    }

    fun isOnline(context: Context): Boolean {
        return repo.isOnline(context)
    }

    fun makePhoneCall(requiredContext: Context, number: String, bundle: Bundle) {
        return repo.makePhoneCall(requiredContext, number, bundle)
    }

    suspend fun bookTheAppointment(appointment: Appointment) {
        return repo.bookTheAppointment(appointment)
    }

    suspend fun makeBookedAppointmentUnAvailable(appointment: Appointment) {
        return repo.makeBookedAppointmentUnAvailable(appointment)
    }

    suspend fun checkBookedAppointments(appointment: Appointment): Boolean {
        return repo.checkBookedAppointments(appointment)
    }
    fun currentUserExist():Boolean {
        return repo.currentUserExist()
    }

}