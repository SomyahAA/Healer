package com.example.healer.ui.fragments.accounts.profile.user_profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.healer.models.User
import com.example.healer.repository.Repository
import de.hdodenhof.circleimageview.CircleImageView

class UserProfileVM :ViewModel() {

    private val repo : Repository = Repository.getInstance()

    fun readUserDataFromFirestore (): LiveData<User> {
        return repo.readUserDataFromFireStore()
    }
    fun uploadPhotoToFirebaseStorage(imageUri: Uri){
        repo.uploadPhotoToFirebaseStorage(imageUri)
    }
    fun getPhotoFromStorage(imageView: CircleImageView){
        repo.getPhotoFromStorage(imageView)
    }
}