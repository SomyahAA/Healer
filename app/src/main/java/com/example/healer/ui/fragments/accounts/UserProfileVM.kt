package com.example.healer.ui.fragments.accounts

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healer.models.User
import com.example.healer.repository.Repository
import kotlinx.coroutines.launch

class UserProfileVM :ViewModel() {

    private val repo : Repository = Repository.getInstace()

    fun userTypeIsUser() :Boolean{
        var state = false
        viewModelScope.launch {
            state = repo.userTypeIsUser()
        }.invokeOnCompletion {
            state
        }
        return false
    }

    fun readUserDataFromFirestore (): LiveData<User> {
        return repo.readUserDataFromFireStore()
    }
}