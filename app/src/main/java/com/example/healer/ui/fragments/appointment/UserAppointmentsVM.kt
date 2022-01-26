package com.example.healer.ui.fragments.appointment

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.healer.models.Appointment
import com.example.healer.models.Psychologist
import com.example.healer.repository.Repository

class UserAppointmentsVM : ViewModel() {

    private val repo = Repository.getInstance()

    fun getUserAppointmentsList(): LiveData<List<Appointment>> {
        val liveDataList = liveData {
            emit(repo.getUserBookAppointmentsList())
        }
        return liveDataList
    }

    suspend fun getPsychologistInfo(psychologistId: String): Psychologist {
        return repo.readPsychologistDataFromFirestore(psychologistId)
    }

    fun makePhoneCall(requiredContext: Context, number: String, bundle: Bundle) {
        return repo.makePhoneCall(requiredContext, number, bundle)
    }

    fun isOnline(context: Context): Boolean {
        return repo.isOnline(context)
    }

}