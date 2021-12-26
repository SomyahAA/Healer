package com.example.healer.ui.fragments.home.psychologist_home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.healer.R
import com.example.healer.databinding.PsyHomeFragmentBinding

class PsyHomeFragment : Fragment() {


    private lateinit var binding: PsyHomeFragmentBinding


    private lateinit var viewModel: PsyHomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = PsyHomeFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[PsyHomeViewModel::class.java]
        binding.setMyAppsBTN.setOnClickListener {
            findNavController().navigate(R.id.action_psyHomeFragment_to_setAppointmentFragment)
        }
    }
}