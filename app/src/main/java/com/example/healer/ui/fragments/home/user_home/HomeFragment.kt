package com.example.healer.ui.fragments.home.user_home

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.healer.R
import com.example.healer.databinding.AvailableAppointmentItemBinding
import com.example.healer.databinding.FragmentHomeBinding
import com.example.healer.databinding.FragmentPsyCardBinding
import com.example.healer.models.Appointment
import com.example.healer.models.Psychologist
import com.google.firebase.auth.FirebaseAuth
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import kotlinx.coroutines.launch
import javax.security.auth.callback.Callback

private const val TAG = "HomeFragment"

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val homeViewModel: HomeViewModel by lazy { ViewModelProvider(this)[HomeViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

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

        private fun updateAppointmentUI(Appointments: List<Appointment>) {
            val appAdapter = AppAdapter(Appointments)
            binding.AvailableAppointmentRV.adapter = appAdapter
        }

        private lateinit var psychologist: Psychologist

        init {
            binding.AvailableAppointmentRV.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

            binding.videoBTN.setOnClickListener {
                if (homeViewModel.isOnline(requireContext())) {
                    findNavController().navigate(R.id.videoCallFragment)
                } else {
                    SettingsDialog.Builder(requireActivity()).build().show()
                }
            }
            binding.cardView.setOnClickListener {
                if (binding.expandableLayout.visibility == View.GONE) {
                    TransitionManager.beginDelayedTransition(binding.cardView, AutoTransition())
                    binding.expandableLayout.visibility = View.VISIBLE
                    Log.d(TAG, "dates: ${psychologist.availableDates}")
                    updateAppointmentUI(psychologist.availableDates)

                } else {
                    TransitionManager.beginDelayedTransition(binding.cardView, AutoTransition())
                    binding.expandableLayout.visibility = View.GONE
                }
            }
        }

        fun bind(psychologist: Psychologist) {
            this.psychologist = psychologist

            binding.sycName.text = psychologist.name
            binding.sycSpecialty.text = psychologist.specialty
            binding.psyExpYears.text = psychologist.experienceYears
            binding.profileImage.load(psychologist.profileImage)
            binding.bio.text = psychologist.bio
            binding.callBTN.setOnClickListener {
                if (auth.currentUser?.uid != null){
                    homeViewModel.makePhoneCall(requireContext(), psychologist.phoneNumber, Bundle())
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

    /*
    remove it from here
     */

    private inner class AAppHolder(val binding: AvailableAppointmentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(appointment: Appointment) {
            Log.e(TAG, "bind: $appointment")
            binding.AvailableAppointmentTV.text = appointment.dateTime
            binding.AvailableAppointmentTV.setOnClickListener {
                AlertDialog.Builder(requireContext())
                    .setTitle("Confirm Booking")
                    .setMessage(" Are you sure you want to book appointment on ${appointment.dateTime} ")
            }
        }
    }

    private inner class AppAdapter(val Appointments: List<Appointment>) :
        RecyclerView.Adapter<AAppHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AAppHolder {
            val binding = AvailableAppointmentItemBinding.inflate(
                layoutInflater,
                parent,
                false
            )
            return AAppHolder(binding)
        }

        override fun onBindViewHolder(holder: AAppHolder, position: Int) {
            val appointment = Appointments[position]
            holder.bind(appointment)
        }

        override fun getItemCount(): Int = Appointments.size
    }

}