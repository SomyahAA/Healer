package com.example.healer.ui.fragments.home.user_home

import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.healer.R
import com.example.healer.databinding.AppointmentItemBinding
import com.example.healer.databinding.FragmentHomeBinding
import com.example.healer.databinding.FragmentPsyCardBinding
import com.example.healer.models.Appointment
import com.example.healer.models.Psychologist
import com.example.healer.repository.Repository
import com.example.healer.utils.Constants.HOME_FRAGMENT_TAG
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val repo = Repository.getInstance()

    private val homeViewModel: HomeViewModel by lazy { ViewModelProvider(this)[HomeViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        var state = false
        lifecycleScope.launch {
            state = repo.userTypeIsUser()
        }
        if (state) {
            findNavController().navigate(R.id.psyHomeFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.psyLiveData().observe(
            viewLifecycleOwner, {
                updateUI(it)
            }
        )

    }

    private fun updateUI(psychologists: List<Psychologist>) {
        val psyAdapter = PsyAdapter(psychologists)
        binding.recyclerView.adapter = psyAdapter
    }

    private inner class PsyHolder(val binding: FragmentPsyCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.ConsultBTN.setOnClickListener {
                if (homeViewModel.isOnline(requireContext())){
                    findNavController().navigate(R.id.videoCallFragment)
                }else {
                   SettingsDialog.Builder(requireActivity()).build().show()
                }
            }
            binding.cardView.setOnClickListener {
                if (binding.expandableLayout.visibility == View.GONE) {
                    TransitionManager.beginDelayedTransition(binding.cardView, AutoTransition())
                    binding.expandableLayout.visibility = View.VISIBLE
                } else {
                    TransitionManager.beginDelayedTransition(binding.cardView, AutoTransition())
                    binding.expandableLayout.visibility = View.GONE
                }
            }
        }
        fun bind(psychologist: Psychologist) {
            if (!homeViewModel.userTypeIsUser()) {
                homeViewModel.psyLiveData().observe(viewLifecycleOwner) {
                    binding.sycName.text = psychologist.name
                    binding.sycSpecialty.text = psychologist.specialty
                    binding.psyExpYears.text = psychologist.experienceYears
                    binding.profileImage.load(psychologist.profileImage)
                    binding.bio.text = psychologist.bio
                }
            }
        }
    }

    private inner class PsyAdapter(val psychologists: List<Psychologist>) :
        RecyclerView.Adapter<PsyHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PsyHolder {
            val binding = FragmentPsyCardBinding.inflate(
                layoutInflater,
                parent,
                false
            )
            return PsyHolder(binding)
        }

        override fun onBindViewHolder(holder: PsyHolder, position: Int) {
            val psy = psychologists[position]
            holder.bind(psy)
        }
        override fun getItemCount(): Int = psychologists.size
    }



}