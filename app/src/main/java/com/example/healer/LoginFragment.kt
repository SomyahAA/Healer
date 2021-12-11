package com.example.healer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth



class LoginFragment : Fragment() {

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var login: Button
    private lateinit var register: TextView

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (auth.currentUser != null) {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        login = view.findViewById(R.id.login)

        email = view.findViewById(R.id.loginEmail)
        password=view.findViewById(R.id.loginPassword)

        register=view.findViewById(R.id.goToRegister)


        login.setOnClickListener {
            if (email.toString().isEmpty() || password.toString().isEmpty()) {
                Toast.makeText(requireContext(), "You must add email and password", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(email.toString(), password.toString())
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {
                            Log.d("healer", "signInUserWithEmail:success")
                            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                        } else {
                            Log.d("healer", "signInUserWithEmail:failure", task.exception)
                            Toast.makeText(requireContext(), "Login failed" + task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
        register.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_userOrPsyFragment)
        }
        return view
    }
}




