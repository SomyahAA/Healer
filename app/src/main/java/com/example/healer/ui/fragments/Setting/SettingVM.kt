package com.example.healer.ui.fragments.Setting
import androidx.lifecycle.ViewModel
import com.example.healer.repository.Repository

class SettingVM:ViewModel() {

    val repo =Repository.getInstance()

     suspend fun deleteAccount(){
         return repo.deleteAccount()
    }
}