package com.example.healer.ui.fragments.accounts.profile.user_profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healer.models.User
import com.example.healer.repository.Repository
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch

class UserProfileVM : ViewModel() {

    private val repo: Repository = Repository.getInstance()

    fun readUserDataFromFirestore(): LiveData<User> {
        return repo.readUserDataFromFireStore()
    }

    fun uploadPhotoToFirebaseStorage(imageUri: Uri) {
        viewModelScope.launch {
            repo.uploadPhotoToFirebaseStorage(imageUri)
        }
    }

    fun updateUserProfile(
        profileImageUrl: String = "",
        name: String = "",
        gender: String = ""
    ) {
        return repo.updateUserProfile(profileImageUrl, name, gender)
    }
}