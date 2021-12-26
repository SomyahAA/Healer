package com.example.healer.ui.fragments.home.user_home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.healer.databinding.FragmentPsyCardBinding


class PsyCardFragment : Fragment() {
    private lateinit var binding: FragmentPsyCardBinding
    private val psyCardVM : PsyCardVM by lazy { ViewModelProvider(this)[PsyCardVM::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPsyCardBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (psyCardVM.userTypeIsUser()) {
            psyCardVM.readPsyDataFromFirestore().observe(viewLifecycleOwner){ psy->
                binding.sycName.text = psy.name
                binding.sycSpecialty.text =psy.specialty
                binding.psyExpYears.text =psy.experienceYears
                psyCardVM.getPhotoFromStorage(binding.profileImage,psy.profileImage)
            }
        }
    }

}