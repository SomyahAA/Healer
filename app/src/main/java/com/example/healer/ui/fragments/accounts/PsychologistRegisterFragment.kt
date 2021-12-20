package com.example.healer.ui.fragments.accounts

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.healer.R
import com.example.healer.databinding.FragmentPsychologistRegisterBinding
import com.example.healer.models.Psychologist
import com.example.healer.utils.Constants.AUTH
import com.example.healer.utils.Constants.FIRE_STORE
import com.example.healer.utils.Constants.PSYCHOLOGIST_REGISTER
import com.example.healer.utils.Constants.USER_REGISTER

class PsychologistRegisterFragment : Fragment() {

    private lateinit var binding: FragmentPsychologistRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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

                AUTH.createUserWithEmailAndPassword(
                    binding.psyEmail.text.toString(),
                    binding.psyPassword.text.toString()
                )
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {
                            Log.d(PSYCHOLOGIST_REGISTER, "createPsyWithEmail:success")

                            FIRE_STORE.collection("PsyUsers")
                                .document(AUTH.currentUser?.uid!!)
                                .set(psychologistModel)
                                .addOnSuccessListener {
                                    Log.d(
                                        USER_REGISTER,
                                        "Done creating user in fireStore successfully"
                                    )
                                }
                                .addOnFailureListener { e ->
                                    Log.w(USER_REGISTER, "Error while adding user in fireStore", e)
                                }
                            findNavController().navigate(R.id.action_psychologistRegisterFragment_to_homeFragment)
                        } else {
                            Log.d(
                                PSYCHOLOGIST_REGISTER,
                                "createUserWithEmail:failure",
                                task.exception
                            )
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