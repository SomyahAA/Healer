package com.example.healer.ui.main

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healer.repository.Repository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MainVM :ViewModel(){

    private val repo = Repository.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()


    suspend fun userTypeIsUser(): Boolean {
        return repo.userTypeIsUser()
    }

    fun getPhotoFromStorage(userUrl: String = auth.currentUser!!.uid): LiveData<Uri>{
        return repo.getPhotoFromStorage(userUrl)
    }

    suspend fun getHeaderNameFromFirebase(): LiveData<String> {
       return repo.getHeaderNameFromFirebase()
    }

    fun setUpRecurringWork(context: Context){
        return repo.setUpRecurringWork(context)
    }

    fun currentUserExist():Boolean {
        return repo.currentUserExist()
    }
    fun signOut(){
        repo.signOut()
    }


}