package com.example.healer.ui.fragments.home.user_home


import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.healer.R
import com.example.healer.databinding.FragmentPsyCardBinding
import com.example.healer.videoCall.VideoCallFragment


class PsyCardFragment : Fragment() {
    private lateinit var binding: FragmentPsyCardBinding
    private val psyCardVM: PsyCardVM by lazy { ViewModelProvider(this)[PsyCardVM::class.java] }

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

        psyCardVM.readPsyDataFromFirestore().observe(viewLifecycleOwner) { psy ->
                binding.sycName.text = psy.name
                binding.sycSpecialty.text = psy.specialty
                binding.psyExpYears.text = psy.experienceYears
            psyCardVM.getPhotoFromStorage(psy.id).observe(viewLifecycleOwner){
                binding.profileImage.load(it)
            }

        }

    }
}