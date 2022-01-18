package com.example.healer.ui.fragments.home.user_home



import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healer.databinding.AvailableAppointmentItemBinding
import com.example.healer.databinding.FragmentPsyCardBinding
import com.example.healer.models.Appointment
import kotlinx.coroutines.launch

  class PsyCardFragment : Fragment() {
//
//      private lateinit var binding: FragmentPsyCardBinding
//
//      private val psyCardVM: PsyCardVM by lazy { PsyCardVM() }
//
//      override fun onCreateView(
//          inflater: LayoutInflater, container: ViewGroup?,
//          savedInstanceState: Bundle?
//      ): View {
//          binding = FragmentPsyCardBinding.inflate(layoutInflater)
//          binding.AvailableAppointmentRV.layoutManager = LinearLayoutManager(context)
//          // Inflate the layout for this fragment
//          return binding.root
//      }
//
//      private inner class AAppHolder(val binding: AvailableAppointmentItemBinding) :
//          RecyclerView.ViewHolder(binding.root) {
//
//
//          fun bind(appointment: Appointment) {
//              lifecycleScope.launch {
//                  psyCardVM.getAppointments(appointment.psychologistId)
//                      .observe(viewLifecycleOwner) {
//                          binding.AvailableAppointmentTV.text = appointment.dateTime
//                          Log.e("hjhbj", appointment.dateTime)
//                      }
//              }
//          }
//      }
//
//      private fun updateAppointmentUI(Appointments: List<Appointment>) {
//          val appAdapter = AppAdapter(Appointments)
//          binding.AvailableAppointmentRV.adapter = appAdapter
//      }
//
//      private inner class AppAdapter(val Appointments: List<Appointment>) :
//          RecyclerView.Adapter<AAppHolder>() {
//          override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AAppHolder {
//              val binding = AvailableAppointmentItemBinding.inflate(
//                  layoutInflater,
//                  parent,
//                  false
//              )
//              return AAppHolder(binding)
//          }
//
//          override fun onBindViewHolder(holder: AAppHolder, position: Int) {
//              val appointment = Appointments[position]
//              holder.bind(appointment)
//          }
//
//          override fun getItemCount(): Int = Appointments.size
//      }
//
////      override fun onExpand(id: String) {
////
////          Log.e("hjhbj", "dasdsd  $id")
////              psyCardVM.getAppointments(id).observeForever{
////                 updateAppointmentUI(it)
////                  Log.e("hjhbj","$it")
////              }
////          }
////      }
}