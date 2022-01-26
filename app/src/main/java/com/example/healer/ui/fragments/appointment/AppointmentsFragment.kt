package com.example.healer.ui.fragments.appointment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.healer.R
import com.example.healer.databinding.FragmentAppointmentsBinding
import com.example.healer.databinding.FragmentDialogBinding
import com.example.healer.databinding.FragmentPsyCardBinding
import com.example.healer.models.Appointment
import com.google.firebase.auth.FirebaseAuth
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import kotlinx.coroutines.launch

class AppointmentsFragment : Fragment() {


    private lateinit var binding: FragmentAppointmentsBinding

    private val userAppointmentsVM: UserAppointmentsVM by lazy { ViewModelProvider(this)[UserAppointmentsVM::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppointmentsBinding.inflate(layoutInflater)
        binding.recyclerViewq.layoutManager = LinearLayoutManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userAppointmentsVM.getUserAppointmentsList().observe(
            viewLifecycleOwner, {
                updateUI(it)
            }
        )
    }

    private fun updateUI(appointments: List<Appointment>) {
        val appAdapter = QAdapter(appointments)
        binding.recyclerViewq.adapter = appAdapter
    }

    private inner class QHolder(val binding: FragmentDialogBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var appointment: Appointment

        fun bind(appointment: Appointment) {
            this.appointment = appointment

            lifecycleScope.launch {
                val psychologist =
                    userAppointmentsVM.getPsychologistInfo(appointment.psychologistId)
                binding.dateTV.text = appointment.dateTime
                binding.name.text = psychologist.name
                binding.speciality.text = psychologist.specialty
                binding.profileImage.load(psychologist.profileImage)
                binding.callBTN.setOnClickListener {
                    userAppointmentsVM.makePhoneCall(
                        requireContext(), psychologist.phoneNumber,
                        Bundle()
                    )
                }
                binding.videoBTN.setOnClickListener {
                    if (userAppointmentsVM.isOnline(requireContext())) {
                        findNavController().navigate(R.id.videoCallFragment)
                    } else {
                        SettingsDialog.Builder(requireActivity()).build().show()
                    }
                }
            }
        }
    }

    private inner class QAdapter(val appointments: List<Appointment>) :
        RecyclerView.Adapter<QHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QHolder {
            val binding = FragmentDialogBinding.inflate(
                layoutInflater,
                parent,
                false
            )
            return QHolder(binding)
        }

        override fun onBindViewHolder(holder: QHolder, position: Int) {
            val appointment = appointments[position]
            holder.bind(appointment)
        }

        override fun getItemCount(): Int = appointments.size
    }

}