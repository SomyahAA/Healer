package com.example.healer.ui.fragments.accounts.profile.psychologist_profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.healer.models.Psychologist
import com.example.healer.models.User
import com.example.healer.repository.Repository
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch

class PsychologistProfileVM : ViewModel() {

    private val repo = Repository.getInstance()


    fun readPsyDataFromFirestore(): LiveData<Psychologist> {
        return liveData {
            emit(repo.readPsychologistDataFromFirestore())
        }
    }

    fun uploadPhotoToFirebaseStorage(imageUri: Uri) {
        viewModelScope.launch {
            repo.uploadPhotoToFirebaseStorage(imageUri)
        }
    }

    fun updatePsyProfile(
        name: String,
        specialty: String,
        bio: String,
        experienceYears: String
    ) {
        return repo.updatePsyProfile(name, specialty, bio, experienceYears)
    }

}