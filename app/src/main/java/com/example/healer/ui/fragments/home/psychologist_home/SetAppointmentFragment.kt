package com.example.healer.ui.fragments.home.psychologist_home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healer.databinding.AppointmentItemBinding
import com.example.healer.databinding.SetAppointmentFragmentBinding
import com.example.healer.models.Appointment
import com.example.healer.utils.Constants.psychologistCollection
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import kotlinx.coroutines.launch


class SetAppointmentFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: SetAppointmentFragmentBinding

    private val fireStore = Firebase.firestore
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val calendar = Calendar.getInstance()
    private var currentMonth = 0
    private val setAppointmentViewModel: SetAppointmentViewModel by lazy { ViewModelProvider(this)[SetAppointmentViewModel::class.java] }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SetAppointmentFragmentBinding.inflate(layoutInflater)
        binding.appointmentRV.layoutManager = LinearLayoutManager(requireContext())


        // set current date to calendar and current month to currentMonth variable
        calendar.time = Date()
        currentMonth = calendar[Calendar.MONTH]

        binding.btnDate.setOnClickListener(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {

            setAppointmentViewModel.getPsychologistAppointments().observe(
                viewLifecycleOwner,{
                    updateUI(it)
                }

            )
        }
    }

    private fun updateUI(Appointments: List<Appointment>) {
        val appAdapter = AppAdapter(Appointments)
        binding.appointmentRV.adapter = appAdapter
    }

    private inner class AppHolder(val binding:  AppointmentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


         fun bind(appointment: Appointment) {
              lifecycleScope.launch {
                  setAppointmentViewModel.getPsychologistAppointments().observe(viewLifecycleOwner) {
                      binding.AppointmentTV.text = appointment.dateTime
                  }
              }
        }
    }

    private inner class AppAdapter(val Appointments: List<Appointment>) :
        RecyclerView.Adapter<AppHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppHolder {
            val binding = AppointmentItemBinding.inflate(
                layoutInflater,
                parent,
                false
            )
            return AppHolder(binding)
        }

        override fun onBindViewHolder(holder: AppHolder, position: Int) {
            val appointment = Appointments[position]
            holder.bind(appointment)
        }
        override fun getItemCount(): Int = Appointments.size
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnDate -> {
                SingleDateAndTimePickerDialog.Builder(context)
                    .bottomSheet()
                    .curved()
                    .mustBeOnFuture()
                    .listener { date ->
                        val appointment = Appointment(date.toString(), auth.currentUser?.uid.toString())
                        setAppointmentViewModel.addAppointment(appointment)
                    }.display()
            }
        }
    }
}