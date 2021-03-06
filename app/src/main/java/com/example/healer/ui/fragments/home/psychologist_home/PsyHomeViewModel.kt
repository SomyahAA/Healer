package com.example.healer.ui.fragments.home.psychologist_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healer.repository.Repository
import kotlinx.coroutines.launch

class PsyHomeViewModel : ViewModel() {

    private val repo = Repository.getInstance()

    suspend fun userTypeIsUser(): Boolean {
           return repo.userTypeIsUser()
    }

    fun currentUserExist():Boolean {
        return repo.currentUserExist()
    }
}