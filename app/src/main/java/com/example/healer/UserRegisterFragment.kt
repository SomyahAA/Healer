package com.example.healer

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
const val TAG ="UserRegister"

val database = Firebase.firestore

class UserRegisterFragment : Fragment() {


    private lateinit var userRegister: Button
    lateinit var name: TextView
    lateinit var phoneNumber: TextView
    lateinit var email: TextView
    lateinit  var gender: TextView
    lateinit var password: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_user_register, container, false)
        userRegister = view.findViewById(R.id.userRegister)
        name =view.findViewById(R.id.userName)
        phoneNumber=view.findViewById(R.id.userPhoneNumber)
        email=view.findViewById(R.id.userEmail)
        gender=view.findViewById(R.id.userGender)
        password=view.findViewById(R.id.userPassword)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        userRegister.setOnClickListener {

            if (name.text.isEmpty()|| phoneNumber.text.isEmpty()|| email.text.isEmpty()
            || gender.text.isEmpty() ||password.text.isEmpty()){
            Toast.makeText(requireContext(), "You must fill all fields", Toast.LENGTH_SHORT).show()
        }

        else{
            val model = User(
                name.text.toString(), phoneNumber.text.toString(), email.text.toString(),  gender.text.toString()
            )
            auth.createUserWithEmailAndPassword( email.text.toString(),password.text.toString())
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        Log.d("healer", "createUserWithEmail:success")


                        val user = hashMapOf(
                            "name" to name.text.toString(),
                            "phone Number" to phoneNumber.text.toString(),
                            "email" to email.text.toString(),
                            "gender" to gender.text.toString(),
                            "profile_image" to ""
                        )
                        database.collection("users")
                            .document(auth.currentUser?.uid!!)
                            .set(user)
                            .addOnSuccessListener {
                                Log.d(TAG, "done added user in firebase")
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error adding document", e)
                            }
                        findNavController().navigate(R.id.action_userRegisterFragment_to_homeFragment)
                    } else {
                        Log.d("healer", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(requireContext(), task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                }

        }
    }
    }
}




