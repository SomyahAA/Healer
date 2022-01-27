package com.example.healer.ui.fragments.home.psychologist_home

import android.app.*
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healer.R
import com.example.healer.databinding.AppointmentItemBinding
import com.example.healer.databinding.SetAppointmentFragmentBinding
import com.example.healer.models.Appointment
import com.example.healer.notification.Notification
import com.example.healer.utils.Constants.channelID
import com.example.healer.utils.Constants.messageExtra
import com.example.healer.utils.Constants.notificationId
import com.example.healer.utils.Constants.titleExtra
import java.util.*
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import kotlinx.coroutines.launch


class SetAppointmentFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: SetAppointmentFragmentBinding

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()

    }

    private fun scheduleNotification() {
        val intent = Intent(requireContext(), Notification::class.java)
        val title = "This is the title "
        val message = getString(R.string.appointment_set)
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(), notificationId, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val time = getTim()
        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent)
        showAlert(time, title, message)
    }

    private fun getTim(): Long {
        return calendar.timeInMillis
    }

    private fun showAlert(time: Long, title: String, message: String) {

        val dateFormat = android.text.format.DateFormat.getLongDateFormat(requireContext())

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.booking_confirmed))
            .setMessage("title" + title + "message" + message + "\n At:" + dateFormat + " ")
    }

    private fun createNotificationChannel() {
        val name = "Notify Channel"
        val desc = "A Description of the channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager =
            requireActivity().getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            setAppointmentViewModel.getPsychologistAppointments().observe(
                viewLifecycleOwner
            ) {
                updateUI(it)
            }
        }
    }

    private fun updateUI(Appointments: List<Appointment>) {
        val appAdapter = AppAdapter(Appointments)
        binding.appointmentRV.adapter = appAdapter
    }

    private inner class AppHolder(val binding: AppointmentItemBinding) :
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
                        val appointment =
                            Appointment(date.toString(),setAppointmentViewModel.getCurrentUserId())

                        lifecycleScope.launch {
                            if (setAppointmentViewModel.appointmentAlreadyExist(appointment)) {
                                DynamicToast.make(
                                    requireContext(),
                                    getString(R.string.app_Already_set)
                                ).show()
                            } else {
                                setAppointmentViewModel.addAppointment(appointment)
                                calendar.set(
                                    date.year,
                                    date.month,
                                    date.day,
                                    date.hours,
                                    date.minutes
                                )
                            }
                        }
                    }.display()
                scheduleNotification()
            }
        }
    }
}