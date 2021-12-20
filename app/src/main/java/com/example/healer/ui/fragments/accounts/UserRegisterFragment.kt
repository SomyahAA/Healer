package com.example.healer.ui.fragments.accounts

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
import com.example.healer.utils.Constants.AUTH
import com.example.healer.utils.Constants.FIRE_STORE
import com.example.healer.utils.Constants.USER_REGISTER

class UserRegisterFragment : Fragment() {

    private lateinit var binding: FragmentUserRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userModel = User(
            binding.userName.text.toString(),
            binding.userPhoneNumber.text.toString(),
            binding.userEmail.text.toString(),
            binding.userGender.text.toString()
        )

        binding.userRegister.setOnClickListener {

            if (binding.userName.text.isEmpty() || binding.userPhoneNumber.text.isEmpty() || binding.userEmail.text.isEmpty()
                || binding.userGender.text.isEmpty() || binding.userPassword.text.isEmpty()
            ) {

                Toast.makeText(requireContext(), "You must fill all fields", Toast.LENGTH_SHORT)
                    .show()
            } else {

                AUTH.createUserWithEmailAndPassword(
                    binding.userEmail.text.toString(),
                    binding.userPassword.text.toString()
                )
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {
                            Log.d("healer", "createUserWithEmail:success")

                            FIRE_STORE.collection("users")
                                .document(AUTH.currentUser?.uid!!)
                                .set(userModel)
                                .addOnSuccessListener {
                                    Log.d(USER_REGISTER, "done added user in firebase")
                                }
                                .addOnFailureListener { e ->
                                    Log.w(USER_REGISTER, "Error adding document", e)
                                }
                            findNavController().navigate(R.id.action_userRegisterFragment2_to_homeFragment2)
                        } else {
                            Log.d("healer", "createUserWithEmail:failure", task.exception)
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




