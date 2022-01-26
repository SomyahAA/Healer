package com.example.healer.ui.fragments.accounts.login

import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.healer.R
import com.example.healer.databinding.FragmentLoginBinding
import com.example.healer.utils.HideKeyboard
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val loginVM: LoginVM by lazy { ViewModelProvider(this)[LoginVM::class.java] }
    private val hideKeyboard = HideKeyboard()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLoginBinding.inflate(layoutInflater)
        val auth: FirebaseAuth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }

        binding.login.setOnClickListener {
            loginVM.login(
                binding.loginEmail.text.toString(),
                binding.loginPassword.text.toString(),
                requireContext()
            )

            if (auth.currentUser != null) {
                if (!loginVM.userTypeIsUser()) {
                    findNavController().navigate(R.id.action_loginFragment_to_psyHomeFragment)
                } else {
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }
            }
        }

        binding.goToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_userRegisterFragment)
        }

        hideKeyboard.setupUI(binding.lololish, requireContext())

        return binding.root
    }
}