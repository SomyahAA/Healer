package com.example.healer.ui.fragments.accounts.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healer.repository.Repository
import kotlinx.coroutines.launch

class LoginVM : ViewModel() {

    private val repo = Repository.getInstance()

    suspend fun userTypeIsUser(): Boolean {
           return repo.userTypeIsUser()
    }

    fun login(loginEmail: String, loginPassword: String, requiredContext: Context) {
        return repo.login(loginEmail, loginPassword, requiredContext)
    }

    fun currentUserExist():Boolean {
        return repo.currentUserExist()
    }

}