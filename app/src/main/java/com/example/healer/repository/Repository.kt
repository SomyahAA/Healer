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
import androidx.work.*
import com.example.healer.models.Appointment
import com.example.healer.utils.Constants.psychologistCollection
import com.example.healer.utils.Constants.usersCollection
import java.util.concurrent.TimeUnit


class Repository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val fireStore = Firebase.firestore
    private val currentUser = auth.currentUser?.uid

    companion object { fun getInstance(): Repository = Repository() }

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
                        Constants.USER_REGISTER_TAG, "createUserWithEmail:failure", task.exception
                    )
                    Toast.makeText(requiredContext, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun registerPsychologist(email: String, password: String,
        psychologistModel: Psychologist, requiredContext: Context) {
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
        val documents = Firebase.firestore.collection(usersCollection)
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

    suspend fun readPsychologistDataFromFirestore(): Psychologist {

        return fireStore.collection(psychologistCollection)
            .document(auth.currentUser!!.uid)
            .get()
            .await()
            .toObject(Psychologist::class.java) ?: Psychologist()
    }

    suspend fun readUserDataFromFireStore(): User {
        return fireStore.collection(usersCollection)
            .document(auth.currentUser!!.uid)
            .get()
            .await()
            .toObject(User::class.java)!!
    }

    suspend fun getAllPsychologist(): List<Psychologist> {
        val psyList = mutableListOf<Psychologist>()
        fireStore.collection(psychologistCollection)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val psychologist = document.toObject(Psychologist::class.java)
                    psyList.add(psychologist)
                }
            }.await()
        return psyList
    }

    suspend fun getAppointments(psychologistId:String = auth.currentUser!!.uid):MutableList<Appointment> {
        val appointment = fireStore.collection(psychologistCollection)
            .document(psychologistId)
            .get()
            .await()
            .toObject(Psychologist::class.java)

        val appointmentList = mutableListOf<Appointment>()
        appointment?.availableDates?.forEach {
            appointmentList += it
        }
        return appointmentList
    }

    suspend fun deleteAppointment(deletedAppointment:Int){
        val appointment = fireStore.collection(psychologistCollection)
            .document(auth.currentUser!!.uid)
            .get()
            .await()
            .toObject(Psychologist::class.java)

        val appointmentList = mutableListOf<Appointment>()

        appointment?.availableDates?.forEach {
            appointmentList += it
        }

        appointmentList.remove(appointmentList[deletedAppointment])

        fireStore.collection(psychologistCollection)
            .document(auth.currentUser!!.uid)
            .update("availableDates",appointmentList)
            .await()
    }

    suspend fun addAppointment(newAppointment: Appointment){

        val appointment =fireStore.collection(psychologistCollection)
            .document(auth.currentUser!!.uid)
            .get()
            .await()
            .toObject(Psychologist::class.java)

        val appointmentList = mutableListOf<Appointment>()

        appointment?.availableDates?.forEach { it ->
            appointmentList += it
        }
        appointmentList.add(newAppointment)

        fireStore.collection(psychologistCollection)
            .document(auth.currentUser!!.uid)
            .update("availableDates",appointmentList)
            .await()
    }

    suspend fun uploadPhotoToFirebaseStorage(imageURI: Uri) {
        val imageRef = FirebaseStorage.getInstance().getReference("/photos/$currentUser")

        val uploadPhoto = imageRef.putFile(imageURI).await()

        if (uploadPhoto.task.isComplete) {

            val j = uploadPhoto.storage.downloadUrl.await()

            if (userTypeIsUser()) {
                fireStore.collection(usersCollection)
                    .document(auth.currentUser!!.uid)
                    .update("profileImage", j.toString())
            } else {
                fireStore.collection(psychologistCollection)
                    .document(auth.currentUser!!.uid)
                    .update("profileImage", j.toString())
            }
        }
    }

    fun getPhotoFromStorage(userUrl: String? = auth.currentUser?.uid): LiveData<Uri> {

        val imageUrl = FirebaseStorage.getInstance().getReference("/photos/$userUrl").downloadUrl
        fireStore.collection(usersCollection)
        val uriLiveData: MutableLiveData<Uri> = MutableLiveData()
        imageUrl.addOnSuccessListener {
            uriLiveData.value = it
        }.addOnFailureListener {
            Log.e(REPOSITORY_TAG, " fail", it)
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
           val user = readUserDataFromFireStore()
            headerNameLiveData.value = user.name

        } else {
            val psychologist = readPsychologistDataFromFirestore()
            headerNameLiveData.value =psychologist.name
        }
        return headerNameLiveData
    }

    fun updatePsyId(uid: String) {
        fireStore.collection(psychologistCollection)
            .document(auth.currentUser!!.uid)
            .update("id", uid)
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

    fun updateUserProfile(name: String , gender: String ) {

        val userFieldMap = mutableMapOf<String, Any>()
        if (name.isNotBlank()) userFieldMap["name"] = name
        if (gender.isNotBlank()) userFieldMap["gender"] = gender

        fireStore.collection(usersCollection)
            .document(auth.currentUser!!.uid)
            .update(userFieldMap)
    }

    fun updatePsyProfile(name: String , specialty: String , bio: String , experienceYears: String) {
        val userFieldMap = mutableMapOf<String, Any>()
        if (name.isNotBlank()) userFieldMap["name"] = name
        if (specialty.isNotBlank()) userFieldMap["specialty"] = specialty
        if (bio.isNotBlank()) userFieldMap["bio"] = bio
        if (experienceYears.isNotBlank()) userFieldMap["experience Years"] = experienceYears

        fireStore.collection(psychologistCollection)
            .document(auth.currentUser!!.uid)
            .update(userFieldMap)
    }

    suspend fun deleteAccount(){
        if (currentUser!= null){
            if (userTypeIsUser()) {
                fireStore.collection(usersCollection)
                    .document(auth.currentUser!!.uid)
                    .delete()
            }
            else{
                fireStore.collection(psychologistCollection)
                    .document(auth.currentUser!!.uid)
                    .delete()
            }
        }
    }

    fun setUpRecurringWork(context: Context) {

        val constraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED).build()

        val workRequest = PeriodicWorkRequestBuilder<Worker>(2, TimeUnit.DAYS)
            .setConstraints(constraint).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "WORK_ID", ExistingPeriodicWorkPolicy.KEEP, workRequest
        )
    }
}

