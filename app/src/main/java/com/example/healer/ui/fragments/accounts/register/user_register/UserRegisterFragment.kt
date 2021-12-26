package com.example.healer.ui.fragments.accounts.register.user_register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.healer.R
import com.example.healer.databinding.FragmentUserRegisterBinding
import com.example.healer.models.User
import com.example.healer.utils.Constants.USER_REGISTER
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserRegisterFragment : Fragment() {

    private lateinit var binding: FragmentUserRegisterBinding
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val fireStore = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.userRegister.setOnClickListener {

            if (binding.userName.text.isEmpty() || binding.userPhoneNumber.text.isEmpty() || binding.userEmail.text.isEmpty()
                || binding.userGender.text.isEmpty() || binding.userPassword.text.isEmpty()
            ) {
                Toast.makeText(requireContext(), "You must fill all fields", Toast.LENGTH_SHORT)
                    .show()
            } else {

                auth.createUserWithEmailAndPassword(
                    binding.userEmail.text.toString(),
                    binding.userPassword.text.toString()
                )
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(USER_REGISTER, "createUserWithEmail:success")

                            //get user input data
                            val user = User(
                                binding.userName.text.toString(),
                                binding.userPhoneNumber.text.toString(),
                                binding.userEmail.text.toString(),
                                binding.userGender.text.toString()
                            )
                            //add user info to fireStore
                            fireStore.collection("users")
                                .document(auth.currentUser?.uid!!)
                                .set(user)
                                .addOnSuccessListener {
                                    Log.d(USER_REGISTER, "done added user in firebase")
                                }
                                .addOnFailureListener { e ->
                                    Log.w(USER_REGISTER, "Error adding document", e)
                                }
                            //if the register is completed navigate to home page
                            findNavController().navigate(R.id.action_userRegisterFragment_to_homeFragment)
                        } else {
                            Log.d(USER_REGISTER, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                requireContext(),
                                task.exception?.localizedMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }
}




