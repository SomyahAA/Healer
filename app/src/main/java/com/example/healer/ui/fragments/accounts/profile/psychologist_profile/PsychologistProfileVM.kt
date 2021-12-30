package com.example.healer.ui.fragments.accounts.profile.psychologist_profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.healer.models.Psychologist
import com.example.healer.repository.Repository
import de.hdodenhof.circleimageview.CircleImageView

class PsychologistProfileVM : ViewModel() {

    private val repo = Repository.getInstance()


    fun readPsyDataFromFirestore(): LiveData<Psychologist> {
        return repo.readPsychologistDataFromFirestore()
    }

    fun uploadPhotoToFirebaseStorage(imageUri: Uri) {
        repo.uploadPhotoToFirebaseStorage(imageUri)
    }

    fun getPhotoFromStorage(imageView: CircleImageView) {
        repo.getPhotoFromStorage(imageView)
    }

}