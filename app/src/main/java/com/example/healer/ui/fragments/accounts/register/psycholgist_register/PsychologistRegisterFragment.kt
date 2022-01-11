package com.example.healer.ui.fragments.accounts.register.psycholgist_register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.healer.R
import com.example.healer.databinding.FragmentPsychologistRegisterBinding
import com.example.healer.models.Psychologist
import com.example.healer.ui.fragments.accounts.login.LoginVM
import com.example.healer.utils.Constants.PSYCHOLOGIST_REGISTER
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PsychologistRegisterFragment : Fragment() {

    private lateinit var binding: FragmentPsychologistRegisterBinding
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val psyRegisterVM: PsyRegisterVM by lazy { ViewModelProvider(this)[PsyRegisterVM::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPsychologistRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerPsy.setOnClickListener {

            if (binding.psyName.text.isEmpty() || binding.psyPhoneNumber.text.isEmpty() || binding.psyEmail.text.isEmpty() || binding.psyPassword.text.isEmpty()
                || binding.psySpeciality.text.isEmpty() || binding.psyExperienceYears.text.isEmpty() || binding.psyBio.text.isEmpty()
            ) {
                Toast.makeText(requireContext(), "You must fill all fields", Toast.LENGTH_SHORT)
                    .show()

            } else {

                val psychologistModel = Psychologist(
                    binding.psyName.text.toString(),
                    binding.psyPhoneNumber.text.toString(),
                    binding.psyEmail.text.toString(),
                    binding.psySpeciality.text.toString(),
                    binding.psyExperienceYears.text.toString(),
                    binding.psyBio.text.toString()
                )
                psyRegisterVM.registerPsychologist(
                    binding.psyEmail.text.toString(),
                    binding.psyPassword.text.toString(),
                    psychologistModel,
                    requireContext()
                )
                if (auth.currentUser != null) {
                    psyRegisterVM.updatePsyId(auth.currentUser!!.uid)
                    findNavController().navigate(R.id.action_psychologistRegisterFragment_to_psyHomeFragment)
                }
            }
        }

    }
}