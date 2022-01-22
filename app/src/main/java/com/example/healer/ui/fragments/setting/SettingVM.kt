package com.example.healer.ui.fragments.setting
import android.content.Context
import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.example.healer.repository.Repository

class SettingVM:ViewModel() {

    val repo =Repository.getInstance()

     suspend fun deleteAccount(){
         return repo.deleteAccount()
    }

    fun sendEmail(context: Context, bundle: Bundle){
        return repo.sendEmail(context,bundle)
    }
}