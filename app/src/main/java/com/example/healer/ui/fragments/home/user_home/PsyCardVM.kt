package com.example.healer.ui.fragments.home.user_home

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healer.models.Psychologist
import com.example.healer.repository.Repository
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch

class PsyCardVM : ViewModel(){


    private val repo = Repository.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()


    fun userTypeIsUser() :Boolean{
        var state = false
        viewModelScope.launch {
            state = repo.userTypeIsUser()
        }.invokeOnCompletion {
            state
        }
        return false
    }

    fun readPsyDataFromFirestore (): LiveData<Psychologist> {
        return repo.readPsyDataFromFirestore()

    }

    fun getPhotoFromStorage(imageView: CircleImageView,currentUser: String? = auth.currentUser?.uid,){
        repo.getPhotoFromStorage(imageView)
    }

}