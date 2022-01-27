package com.example.healer.ui.fragments.home.psychologist_home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healer.databinding.PsyBookedAppointmentsBinding
import com.example.healer.models.Appointment
import kotlinx.coroutines.launch


class PsyBookedAppointments : Fragment() {

    // this is still onProgress
    private lateinit var binding: PsyBookedAppointmentsBinding

    private val psyBookedAppVM: PsyBookedAppVM by lazy { ViewModelProvider(this)[PsyBookedAppVM::class.java] }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = PsyBookedAppointmentsBinding.inflate(layoutInflater)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            psyBookedAppVM.getPsyBookedAppList().observe(
                viewLifecycleOwner
            ) {
                updateUI(it)
            }
        }
    }

    private fun updateUI(appointments: List<Appointment>) {
        val appAdapter = BAAdapter(appointments)
        binding.recyclerView.adapter = appAdapter
    }

    private inner class BAHolder( binding: PsyBookedAppointmentsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var appointment: Appointment

        fun bind(appointment: Appointment) {
            this.appointment = appointment
        }
    }

    private inner class BAAdapter(val appointments: List<Appointment>) :
        RecyclerView.Adapter<BAHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BAHolder {
            val binding = PsyBookedAppointmentsBinding.inflate(
                layoutInflater,
                parent,
                false
            )
            return BAHolder(binding)
        }

        override fun onBindViewHolder(holder: BAHolder, position: Int) {
            val appointment = appointments[position]
            holder.bind(appointment)
        }

        override fun getItemCount(): Int = appointments.size
    }
}