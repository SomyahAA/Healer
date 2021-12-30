package com.example.healer.ui.fragments.accounts.register.user_register

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.healer.models.User
import com.example.healer.repository.Repository

class UserRegisterVM : ViewModel() {

    private val repo = Repository.getInstance()

    fun registerUser(email: String, password: String, user: User, requiredContext: Context) {
        return repo.registerUser(email, password, user, requiredContext)
    }
}