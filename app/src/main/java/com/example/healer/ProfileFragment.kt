package com.example.healer

import android.os.Bundle
import android.provider.DocumentsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.setContentView
import com.example.healer.databinding.ActivityMainBinding
import com.example.healer.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentUser = auth.currentUser!!

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater)

        return binding.root
    }
    private lateinit var currentUser:FirebaseUser

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        readUserDataFromFireStore()
    }

    private fun readUserDataFromFireStore (){
//        val queryDocumentSnapshots : QueryDocumentSnapshot
//        DocumentSnapshot snapshot: snapshotList
//        val snapshotList: List<DocumentSnapshot> = queryDocumentSnapshots.getDocuments()
//
//        const snapshot = database.collection("users").get()
//        return snapshot.docs.map(doc => doc.data())

//        database.collection("users").get()
//            .addOnSuccessListener{ documents ->
//                for(document in documents){
//                    if (document.toString() == currentUser.uid){
//                        Log.d(TAG,"${document.id}=>${document.data}")
//                    }
//                }
//            }
//            .addOnFailureListener{exception ->
//                Log.w(TAG,"Error getting documents:",exception)
//            }
        database.collection("users")
            .document(currentUser.uid)
            .get()
            .addOnSuccessListener { it ->
                val dataList = it.data
                dataList?.forEach {
                    //Log.d("fromProfile" , "data is $it")
                    //it["name"]
                    when(it.key) {
                        "name" -> binding.userName.setText(it.value.toString())
                        "phone Number" -> binding.userPhoneNumber.setText(it.value.toString())
                        "gender" -> binding.userGender.setText(it.value.toString())
                        //"profile_image" -> binding.profileImage.setImageIcon()
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "could not load user info from fireStore", e)
            }
    }

}