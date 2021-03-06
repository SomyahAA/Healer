package com.example.healer.ui.fragments.accounts.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.healer.R
import com.example.healer.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val loginVM: LoginVM by lazy { ViewModelProvider(this)[LoginVM::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLoginBinding.inflate(layoutInflater)

        if (loginVM.currentUserExist()) {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }

        binding.login.setOnClickListener {
            loginVM.login(
                binding.loginEmail.text.toString(),
                binding.loginPassword.text.toString(),
                requireContext()
            )

            if (loginVM.currentUserExist()) {
                lifecycleScope.launch {

                if (!loginVM.userTypeIsUser()) {
                    findNavController().navigate(R.id.action_loginFragment_to_psyHomeFragment)
                } else {
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }
            }
            }
        }

        binding.goToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_userRegisterFragment)
        }

        return binding.root
    }
}