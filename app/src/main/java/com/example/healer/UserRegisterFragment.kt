package com.example.healer

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
import com.google.firebase.ktx.Firebase


class UserRegisterFragment : Fragment() {


    private val userAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private var database: DatabaseReference = Firebase.database.reference

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
                name.toString(), phoneNumber.toString(), email.toString(),  gender.toString(),
                password.toString()
            )
            userAuth.createUserWithEmailAndPassword(email.toString(), password.toString())
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        Log.d("healer", "createUserWithEmail:success")

                        database.child("User Information")
                            .child(userAuth.currentUser?.uid!!).setValue(model)
                        Toast.makeText(requireContext(), "Registration Successful", Toast.LENGTH_SHORT).show()
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




