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

class PsyCardVM : ViewModel() {


    private val repo = Repository.getInstance()

    fun readPsyDataFromFirestore(): LiveData<Psychologist> {
        return repo.readPsychologistDataFromFirestore()
    }

    fun getPhotoFromStorage(currentUser: String?) :LiveData<Uri>{
       return repo.getPhotoFromStorage(currentUser)
    }

}