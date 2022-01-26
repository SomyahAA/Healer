package com.example.healer.ui.fragments.accounts.register.psychologist_register

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.healer.models.Psychologist
import com.example.healer.repository.Repository

class PsyRegisterVM : ViewModel() {

    private val repo = Repository.getInstance()


    fun registerPsychologist(
        email: String,
        password: String,
        psychologistModel: Psychologist,
        requiredContext: Context
    ) {
        return repo.registerPsychologist(email, password, psychologistModel, requiredContext)
    }

    fun updatePsyId(uid: String) {
        repo.updatePsyId(uid)
    }

    fun currentUserExist():Boolean {
        return repo.currentUserExist()
    }

    fun getCurrentUserId(): String {
        return repo.getCurrentUserId()
    }

}