package com.example.healer.ui.fragments.accounts

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healer.models.Psychologist
import com.example.healer.repository.Repository
import kotlinx.coroutines.launch

class PsychologistProfileVM: ViewModel() {

    private val repo = Repository.getInstace()

    fun userTypeIsUser() :Boolean{
        var state = false
        viewModelScope.launch {
            state = repo.userTypeIsUser()
        }.invokeOnCompletion {
            state
        }
        return false

    }

    fun readPsyDataFromFirestore (): LiveData<Psychologist> {
        return repo.readPsyDataFromFirestore()
    }

}