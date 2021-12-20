package com.example.healer.repository

import android.util.Log
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.healer.models.Psychologist
import com.example.healer.models.User
import com.example.healer.utils.Constants
import com.example.healer.utils.Constants.FIRE_STORE
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.MutableData
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SnapshotMetadata
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.okhttp.internal.DiskLruCache
import kotlinx.coroutines.tasks.await

private val auth: FirebaseAuth = FirebaseAuth.getInstance()
private const val TAG = "Repository"

class Repository {

    companion object{
        fun getInstace():Repository = Repository()
    }

    suspend  fun userTypeIsUser(): Boolean {
        var state = false

       val documents =  Firebase.firestore.collection("users")
            .get().await()
               for (document in documents) {
                   if (document.id == auth.currentUser!!.uid) {
                      state = true
                   }
               }
       return state
    }

    fun readPsyDataFromFirestore(): LiveData<Psychologist> {

        val psychologistLiveData : MutableLiveData<Psychologist> = MutableLiveData()

        FIRE_STORE.collection("PsyUsers")
            .document(auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { it ->
                val dataList = it.data
                val psychologist = Psychologist()
                dataList?.forEach {
                    when (it.key) {
                        "name" -> psychologist.name = it.value as String
                        "phone Number" -> psychologist.phoneNumber =it.value as String
                        "bio" -> psychologist.bio = it.value as String
                        "email" -> psychologist.email =it.value as String
                        "specialty" -> psychologist.specialty =it.value as String
                        "experienceYears" ->psychologist.experienceYears =it.value as String
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "could not load user info from fireStore", e)
            }
        return psychologistLiveData
    }

    fun readUserDataFromFireStore() :LiveData<User> {
        val userLiveData:MutableLiveData<User> = MutableLiveData()
        FIRE_STORE.collection("users")
            .document(auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { it ->
                val dataList = it.data
                val user = User()
                dataList?.forEach {
                    when (it.key) {
                        "name" -> {
                            user.name = it.value as String
                            Log.d(TAG ,"testing" + it.value.toString())
                        }
                        "phone Number" -> user.phoneNumber= it.value as String
                        "gender" -> user.gender =it.value as String
                    }
                }
                userLiveData.value = user
            }
            .addOnFailureListener { e ->
                Log.w(Constants.GET_FROM_FB_STORAGE, "could not load user info from fireStore", e)
            }
        return userLiveData
    }

   suspend fun getAllPsys() :List<Psychologist> {

       val dataList = mutableMapOf<String , Any>()
       val psyList = mutableListOf<Psychologist>()


       FIRE_STORE.collection("PsyUsers")
           .get()
           .addOnSuccessListener { result ->

               for (document in result) {
                   val psychologist = Psychologist()
                   dataList[document.id]=document.data
                   psychologist.name = document.getString("name").toString()
                   psychologist.specialty = document.getString("specialty").toString()
                   psychologist.experienceYears = document.getString("experience Years").toString()
                   psyList.add(psychologist)
                   //Log.d(TAG, "we are adding${document.data} in ${document.id}")
                   Log.d(TAG, " we are getting all the $psyList")
               }

           }.await()
       return psyList
   }
}
//       dataList.forEach {
//           val psychologist = Psychologist()
//
//           when (it.key) {
//
////               "name" -> psychologist.name = (it.value == "name") as String
////               "specialty" -> psychologist.specialty =it.value as String
////               "experience Years" ->psychologist.experienceYears =it.value as String
//           }
//           psyList.add(psychologist)
//           Log.d(TAG, "we are adding${it.value}")
//       }


//       val documents = FIRE_STORE.collection("PsyUsers").get().await()
//       val dList = documents.documents
//       val psyList = mutableListOf<Psychologist>()
//       val dataList = mutableMapOf<String , Any>()
//
//       dList.forEach { it.data?.let { it1 -> dataList.put(it1.keys.toString(),it1.values) } }
//
//       dataList.forEach {
//
//           val psychologist = Psychologist()
//
//           when (it.key) {
//               "name" -> psychologist.name = it.value as String
//               "specialty" -> psychologist.specialty =it.value as String
//               "experience Years" ->psychologist.experienceYears =it.value as String
//           }
//           psyList.add(psychologist)
//       }
//
//       Log.d(TAG , "d $psyList")
//
//        return psyList
//    }

//}
