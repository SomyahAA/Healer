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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.healer.R
import com.example.healer.databinding.AvailableAppointmentItemBinding
import com.example.healer.databinding.FragmentPsyCardBinding
import com.example.healer.models.Appointment
import com.example.healer.videoCall.VideoCallFragment
import kotlinx.coroutines.launch

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

        lifecycleScope.launch {

            psyCardVM.getPsychologistAppointments().observe(
                viewLifecycleOwner,{
                    updateAppointmentUI(it)
                }
            )
        }
    }

    private inner class AAppHolder(val binding: AvailableAppointmentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(appointment: Appointment) {
            lifecycleScope.launch {
                psyCardVM.getPsychologistAppointments().observe(viewLifecycleOwner) {
                    binding.AvailableAppointmentTV.text = appointment.dateTime
                }
            }
        }
    }

    private fun updateAppointmentUI(Appointments: List<Appointment>) {
        val appAdapter = AppAdapter(Appointments)
        binding.AvailableAppointmentRV.adapter = appAdapter
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