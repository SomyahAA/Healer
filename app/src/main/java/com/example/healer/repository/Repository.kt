package com.example.healer.repository


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
import coil.load
import com.example.healer.utils.Constants.REPOSITORY_TAG
import de.hdodenhof.circleimageview.CircleImageView


class Repository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val fireStore = Firebase.firestore
    private val  currentUser = auth.currentUser?.uid

    companion object {
        fun getInstance(): Repository = Repository()
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

    fun readPsyDataFromFirestore(): LiveData<Psychologist> {

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
                        "profileImage" -> user.profileImage= it.value.toString()
                    }
                }
                userLiveData.value = user
            }
            .addOnFailureListener { e ->
                Log.w(Constants.GET_FROM_FB_STORAGE, "could not load user info from fireStore", e)
            }
        return userLiveData
    }

    suspend fun getAllPsy(): List<Psychologist> {

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
                    psychologist.profileImage=document.getString("profileImage").toString()
                    psyList.add(psychologist)

                    //Log.d( repository," adding${document.data} in ${document.id} is done successfully")
                    //Log.d(repository, " we are getting all the $psyList")
                }
            }.await()
        return psyList
    }

     fun uploadPhotoToFirebaseStorage(imageURI: Uri){
        val imageRef = FirebaseStorage.getInstance().getReference("/photos/$currentUser")

        imageRef.putFile(imageURI)
            .addOnSuccessListener {
                //Log.d(repository,"Successfully uploaded the selected image into the firebase storage:${it.metadata?.path}")
                imageRef.downloadUrl.addOnSuccessListener {
                    saveImageToFireStore(it.toString())
                }
            }
            .addOnFailureListener {
                Log.d(REPOSITORY_TAG,"error occur uploading the image to firebase storage")
            }
    }

    private fun saveImageToFireStore(profileImageUrl: String) {
        fireStore.collection("users")
            .document(auth.currentUser!!.uid)
            .update("profileImage",profileImageUrl)

    }

     fun getPhotoFromStorage(image: CircleImageView,userUrl: String? = auth.currentUser?.uid){
       val imageUrl = FirebaseStorage.getInstance().getReference("/photos/$userUrl").downloadUrl
        imageUrl.addOnSuccessListener {
            image.load(it)
        }
    }

}

