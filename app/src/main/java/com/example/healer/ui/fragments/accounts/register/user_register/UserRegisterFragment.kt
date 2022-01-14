package com.example.healer.ui.fragments.accounts.register.user_register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.healer.R
import com.example.healer.databinding.FragmentUserRegisterBinding
import com.example.healer.models.User
import com.google.firebase.auth.FirebaseAuth


class UserRegisterFragment : Fragment() {

    private lateinit var binding: FragmentUserRegisterBinding
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val userRegisterVM: UserRegisterVM by lazy { ViewModelProvider(this)[UserRegisterVM::class.java] }

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



            if (RegistrationUtil.validateRegistrationInput(binding.userName.text.toString(),binding.userPhoneNumber.text.toString()
                ,binding.userEmail.text.toString(),binding.userGender.text.toString(),binding.userPassword.text.toString()))
           {
                Toast.makeText(requireContext(), "You must fill all fields", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val user = User(
                    binding.userName.text.toString(),
                    binding.userPhoneNumber.text.toString(),
                    binding.userEmail.text.toString(),
                    binding.userGender.text.toString()
                )
                userRegisterVM.registerUser(
                    binding.userEmail.text.toString(),
                    binding.userPassword.text.toString(), user, requireContext()
                )

                if (auth.currentUser != null) {
                    findNavController().navigate(R.id.action_userRegisterFragment_to_homeFragment)
                }
            }

        }
    }
}




