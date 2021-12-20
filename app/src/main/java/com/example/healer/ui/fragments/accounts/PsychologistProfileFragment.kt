package com.example.healer.ui.fragments.accounts

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.healer.databinding.FragmentPsychologistProfileBinding



class PsychologistProfileFragment : Fragment() {

    private lateinit var binding: FragmentPsychologistProfileBinding

    private val psychologistProfileVM :PsychologistProfileVM by lazy { ViewModelProvider(this)[PsychologistProfileVM::class.java] }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPsychologistProfileBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            psychologistProfileVM.readPsyDataFromFirestore().observe(viewLifecycleOwner){ psy->

                binding.psyName.setText(psy.name)
                binding.psyPhoneNumber.setText(psy.phoneNumber)
                binding.psyExperienceYears.setText(psy.experienceYears)
                binding.psySpecialty.setText(psy.specialty)
                binding.psyBio.setText(psy.bio)


        }
    }


}