package com.example.healer.repository

import android.Manifest.permission.CALL_PHONE
import android.app.Activity
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.healer.models.Psychologist
import com.example.healer.models.User
import com.example.healer.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import com.example.healer.utils.Constants.REPOSITORY_TAG
import androidx.core.content.ContextCompat.startActivity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import com.example.healer.utils.Constants.LOGIN_TAG
import com.example.healer.utils.Constants.REQUEST_CALL


import android.net.ConnectivityManager
import android.net.NetworkCapabilities


class Repository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val fireStore = Firebase.firestore
    private val currentUser = auth.currentUser?.uid

    companion object {
        fun getInstance(): Repository = Repository()
    }

    fun login(loginEmail: String, loginPassword: String, requiredContext: Context) {
        if (loginEmail.isEmpty() || loginPassword.isEmpty()) {
            Toast.makeText(requiredContext, "You must add email and password", Toast.LENGTH_SHORT)
                .show()
        } else {
            auth.signInWithEmailAndPassword(loginEmail, loginPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(LOGIN_TAG, "signInUserWithEmail:success")
                    } else {
                        Log.d(LOGIN_TAG, "signInUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            requiredContext,
                            "Login failed" + task.exception?.localizedMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    fun registerUser(email: String, password: String, user: User, requiredContext: Context) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(Constants.USER_REGISTER_TAG, "createUserWithEmail:success")

                    //add user info to fireStore
                    fireStore.collection("users")
                        .document(auth.currentUser?.uid!!)
                        .set(user)
                        .addOnSuccessListener {
                            Log.d(Constants.USER_REGISTER_TAG, "done added user in firebase")
                        }
                        .addOnFailureListener { e ->
                            Log.w(Constants.USER_REGISTER_TAG, "Error adding document", e)
                        }
                } else {
                    Log.d(
                        Constants.USER_REGISTER_TAG,
                        "createUserWithEmail:failure",
                        task.exception
                    )
                    Toast.makeText(
                        requiredContext,
                        task.exception?.localizedMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun registerPsychologist(
        email: String,
        password: String,
        psychologistModel: Psychologist,
        requiredContext: Context
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(Constants.PSYCHOLOGIST_REGISTER, "createPsyWithEmail:success")

                    fireStore.collection("PsyUsers")
                        .document(auth.currentUser?.uid!!)
                        .set(psychologistModel)
                        .addOnSuccessListener {
                            Log.d(
                                Constants.PSYCHOLOGIST_REGISTER,
                                "Done creating user in fireStore successfully"
                            )
                        }
                        .addOnFailureListener { e ->
                            Log.w(
                                Constants.PSYCHOLOGIST_REGISTER,
                                "Error while adding user in fireStore",
                                e
                            )
                        }
                } else {
                    Log.d(
                        Constants.PSYCHOLOGIST_REGISTER,
                        "createUserWithEmail:failure",
                        task.exception
                    )
                    Toast.makeText(
                        requiredContext,
                        task.exception?.localizedMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    suspend fun userTypeIsUser(): Boolean {
        var state = false
        val documents = Firebase.firestore.collection("users")
            .get().await()
        for (document in documents) {
            if (auth.currentUser != null) {
                if (document.id == auth.currentUser!!.uid) {
                    state = true
                }
            }
        }
        return state
    }

    fun readPsychologistDataFromFirestore(): LiveData<Psychologist> {

        val psychologistLiveData: MutableLiveData<Psychologist> = MutableLiveData()

        fireStore.collection("PsyUsers")
            .document(auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { it ->
                val dataList = it.data
                val psychologist = Psychologist()
                dataList?.forEach {
                    when (it.key) {
                        "name" -> psychologist.name = it.value as String
                        "phone Number" -> psychologist.phoneNumber = it.value as String
                        "bio" -> psychologist.bio = it.value as String
                        "email" -> psychologist.email = it.value as String
                        "specialty" -> psychologist.specialty = it.value as String
                        "experienceYears" -> psychologist.experienceYears = it.value as String
                        "profileImage" -> psychologist.profileImage = it.value as String
                    }
                }
                psychologistLiveData.value = psychologist
            }
            .addOnFailureListener { e ->

                Log.w(REPOSITORY_TAG, "could not load user info from fireStore", e)
            }
        return psychologistLiveData
    }

    fun readUserDataFromFireStore(): LiveData<User> {

        val userLiveData: MutableLiveData<User> = MutableLiveData()

        fireStore.collection("users")
            .document(auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { it ->
                val dataList = it.data
                val user = User()
                dataList?.forEach {
                    when (it.key) {
                        "name" -> {
                            user.name = it.value as String
                            Log.d(REPOSITORY_TAG, "testing" + it.value.toString())
                        }
                        "phone Number" -> user.phoneNumber = it.value as String
                        "gender" -> user.gender = it.value as String
                        "profileImage" -> user.profileImage = it.value.toString()
                    }
                }
                userLiveData.value = user
            }
            .addOnFailureListener { e ->
                Log.w(Constants.GET_FROM_FB_STORAGE, "could not load user info from fireStore", e)
            }
        return userLiveData
    }

    suspend fun getAllPsychologist(): List<Psychologist> {

        val dataList = mutableMapOf<String, Any>()
        val psyList = mutableListOf<Psychologist>()

        fireStore.collection("PsyUsers")
            .get()
            .addOnSuccessListener { result ->

                for (document in result) {
                    val psychologist = Psychologist()
                    dataList[document.id] = document.data
                    psychologist.name = document.getString("name").toString()
                    psychologist.specialty = document.getString("specialty").toString()
                    psychologist.experienceYears = document.getString("experience Years").toString()
                    psychologist.profileImage = document.getString("profileImage").toString()
                    psyList.add(psychologist)

                    //Log.d( repository," adding${document.data} in ${document.id} is done successfully")
                    //Log.d(repository, " we are getting all the $psyList")
                }
            }.await()
        return psyList
    }

    suspend fun uploadPhotoToFirebaseStorage(imageURI: Uri) {
        val imageRef = FirebaseStorage.getInstance().getReference("/photos/$currentUser")

        val o =imageRef.putFile(imageURI).await()

        if (o.task.isComplete){
            val j= o.storage.downloadUrl.await()

            if (userTypeIsUser()) {
                fireStore.collection("users")
                    .document(auth.currentUser!!.uid)
                    .update("profileImage", j.toString())
            } else {
                fireStore.collection("PsyUsers")
                    .document(auth.currentUser!!.uid)
                    .update("profileImage", j.toString())
            }
        }
    }

    fun getPhotoFromStorage(userUrl: String? = auth.currentUser?.uid): LiveData<Uri> {
        val imageUrl = FirebaseStorage.getInstance().getReference("/photos/$userUrl").downloadUrl
        fireStore.collection("users")
        val uriLiveData: MutableLiveData<Uri> = MutableLiveData()
        imageUrl.addOnSuccessListener {
            uriLiveData.value = it
        }.addOnFailureListener{
                Log.e(REPOSITORY_TAG," fail",it)
        }
        return uriLiveData
    }

    fun makePhoneCall(requiredContext: Context, number: String, bundle: Bundle) {

        if (ContextCompat.checkSelfPermission(
                requiredContext,
                CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(requiredContext as Activity, arrayOf(CALL_PHONE), REQUEST_CALL)
        } else {
            startActivity(
                requiredContext,
                Intent(Intent.ACTION_CALL, Uri.parse("tel:$number")),
                bundle
            )
        }
    }

    suspend fun getHeaderNameFromFirebase(): LiveData<String> {
        val headerNameLiveData: MutableLiveData<String> = MutableLiveData()

        if (userTypeIsUser()) {
            fireStore.collection("Users")
                .document(auth.currentUser!!.uid)
                .get()
                .addOnSuccessListener { it ->
                    it.data?.forEach {
                        when (it.key) {
                            "name" -> headerNameLiveData.value = it.value.toString()
                        }
                    }
                }
        } else {
            fireStore.collection("PsyUsers")
                .document(auth.currentUser!!.uid)
                .get()
                .addOnSuccessListener { it ->
                    it.data?.forEach {
                        when (it.key) {
                            "name" -> headerNameLiveData.value = it.value.toString()
                        }
                    }
                }
        }
        return headerNameLiveData
    }

    fun updatePsyId(uid :String){
        fireStore.collection("PsyUsers")
            .document(auth.currentUser!!.uid)
            .update("id",uid)
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    fun updateUserProfile(
        profileImageUrl: String = "",
        name: String = "",
        email: String = "",
        gender: String = ""
    ) {

        val userFieldMap = mutableMapOf<String, Any>()
        if (profileImageUrl.isNotBlank()) userFieldMap["profileImage"] = profileImageUrl
        if (name.isNotBlank()) userFieldMap["name"] = name
        if (email.isNotBlank()) userFieldMap["email"] = email
        if (gender.isNotBlank()) userFieldMap["gender"] = gender

        fireStore.collection("users")
            .document(auth.currentUser!!.uid)
            .update(userFieldMap)
    }

    fun updatePsyProfile(
        profileImageUrl: String = "",
        name: String = "",
        email: String = "",
        gender: String = "",
        specialty: String = "",
        bio: String = "",
        experienceYears: String = ""
    ) {

        val userFieldMap = mutableMapOf<String, Any>()
        if (profileImageUrl.isNotBlank()) userFieldMap["profileImage"] = profileImageUrl
        if (name.isNotBlank()) userFieldMap["name"] = name
        if (email.isNotBlank()) userFieldMap["email"] = email
        if (gender.isNotBlank()) userFieldMap["gender"] = gender
        if (specialty.isNotBlank()) userFieldMap["specialty"] = specialty
        if (bio.isNotBlank()) userFieldMap["bio"] = bio
        if (experienceYears.isNotBlank()) userFieldMap["experienceYears"] = experienceYears

        fireStore.collection("PsyUsers")
            .document(auth.currentUser!!.uid)
            .update(userFieldMap)
    }

}

