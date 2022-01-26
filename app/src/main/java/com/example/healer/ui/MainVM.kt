package com.example.healer.ui

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healer.repository.Repository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asStateFlow

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainVM :ViewModel(){

    private val repo = Repository.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()


    fun userTypeIsUser(): Boolean {
        var state = false
        viewModelScope.launch {
            state = repo.userTypeIsUser()
        }.invokeOnCompletion {
            state
        }
        return false
    }

    fun getPhotoFromStorage(userUrl: String = auth.currentUser!!.uid): LiveData<Uri>{
        return repo.getPhotoFromStorage(userUrl)
    }
//    private val _isLoading = MutableStateFlow(true)
//    val _isLoading = _isLoading.asSta
//    init {
//        viewModelScope.launch {
//            delay(3000)
//            _isLoading
//        }
//    }
}