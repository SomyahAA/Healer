package com.example.healer.ui.fragments.accounts.profile.user_profile

import android.net.Uri
import androidx.lifecycle.*
import com.example.healer.models.User
import com.example.healer.repository.Repository
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch

class UserProfileVM : ViewModel() {

    private val repo: Repository = Repository.getInstance()

    fun readUserDataFromFirestore(): LiveData<User> {
        return liveData {
           emit(repo.readUserDataFromFireStore())
        }
    }

    fun uploadPhotoToFirebaseStorage(imageUri: Uri) {
        viewModelScope.launch {
            repo.uploadPhotoToFirebaseStorage(imageUri)
        }
    }

    fun updateUserProfile(name: String , gender: String,phoneNumber: String) {
        return repo.updateUserProfile(name, gender,phoneNumber)
    }

}