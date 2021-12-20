package com.example.healer.utils

import android.annotation.SuppressLint
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object Constants {
    const val USER_REGISTER ="UserRegister"
    const val GET_FROM_FB_STORAGE= "getFromFirebaseStorage"
    const val PSYCHOLOGIST_REGISTER="psychologistRegister"
    const val PROFILE_FRAGMENT = "ProfileFragment"
    val AUTH: FirebaseAuth = FirebaseAuth.getInstance()
    val FIRE_STORE = Firebase.firestore
    //val CURRENT_USER =AUTH.currentUser!!
    const val SIGN_IN ="signIn"
}