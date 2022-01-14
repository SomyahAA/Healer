package com.example.healer.ui.fragments.accounts.register.psychologist_register

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.healer.models.Psychologist
import com.example.healer.repository.Repository

class PsyRegisterVM : ViewModel() {

    private val repo = Repository.getInstance()

//    fun addPsyToHomePage(){
//        //repo.addPsyToHomePage()
//    }

    fun registerPsychologist(
        email: String,
        password: String,
        psychologistModel: Psychologist,
        requiredContext: Context
    ) {
        return repo.registerPsychologist(email, password, psychologistModel, requiredContext)
    }

    fun updatePsyId(uid :String){
        repo.updatePsyId(uid)
    }

}