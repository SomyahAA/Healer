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
import com.example.healer.databinding.FragmentLoginBinding
import com.example.healer.utils.Constants.AUTH
import com.example.healer.utils.Constants.SIGN_IN
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        val auth: FirebaseAuth = FirebaseAuth.getInstance()


        if (auth.currentUser != null) {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }

        binding.login.setOnClickListener {
            if (binding.loginEmail.text.toString().isEmpty() || binding.loginPassword.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "You must add email and password", Toast.LENGTH_SHORT).show()
            } else {
                AUTH.signInWithEmailAndPassword(binding.loginEmail.text.toString(), binding.loginPassword.text.toString())
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {
                            Log.d(SIGN_IN, "signInUserWithEmail:success")
                            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                        } else {
                            Log.d(SIGN_IN, "signInUserWithEmail:failure", task.exception)
                            Toast.makeText(requireContext(), "Login failed" + task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
        binding.goToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_userRegisterFragment2)
        }
        return binding.root
    }
}