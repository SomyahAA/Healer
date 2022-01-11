package com.example.healer.ui.fragments.home.psychologist_home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.app.TimePickerDialog
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.healer.R
import com.example.healer.databinding.CalendarItemBinding
import com.example.healer.databinding.FragmentPsyCardBinding
import com.example.healer.databinding.SetAppointmentFragmentBinding
import com.example.healer.models.Psychologist
import com.example.healer.ui.fragments.AppointmentsFragment
import com.example.healer.utils.Constants.APPOINTMENTS_FRAGMENT_TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import com.michalsvec.singlerowcalendar.utils.DateUtils
import com.vmadalin.easypermissions.dialogs.SettingsDialog


class SetAppointmentFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: SetAppointmentFragmentBinding

    private val fireStore = Firebase.firestore
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val calendar = Calendar.getInstance()
    private var currentMonth = 0
    private lateinit var calendarItemBinding: CalendarItemBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SetAppointmentFragmentBinding.inflate(layoutInflater)
        binding.appRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        calendarItemBinding = DataBindingUtil.inflate(inflater, R.layout.calendar_item, container, false)


        // set current date to calendar and current month to currentMonth variable
        calendar.time = Date()
        currentMonth = calendar[Calendar.MONTH]

        binding.btnDate.setOnClickListener(this)
        binding.setAppointment.setOnClickListener(this)

        // calendar view manager is responsible for our displaying logic
        val myCalendarViewManager = object : CalendarViewManager {
            override fun setCalendarViewResourceId(
                position: Int,
                date: Date,
                isSelected: Boolean
            ): Int {
                // set date to calendar according to position where we are
                val cal = Calendar.getInstance()
                cal.time = date
                // if item is selected we return this layout items
                // in this example. monday, wednesday and friday will have special item views and other days
                // will be using basic item view
                return if (isSelected){
                    R.layout.selected_calendar_item
                }
                else{
                    // here we return items which are not selected
                    R.layout.calendar_item
                }

                // NOTE: if we don't want to do it this way, we can simply change color of background
                // in bindDataToCalendarView method
            }

            override fun bindDataToCalendarView(

                holder: SingleRowCalendarAdapter.CalendarViewHolder,
                date: Date,
                position: Int,
                isSelected: Boolean
            ) {
                // using this method we can bind data to calendar view
                // good practice is if all views in layout have same IDs in all item views



                /*holder.itemView.tv_date_calendar_item.text = DateUtils.getDayNumber(date)
                holder.itemView.tv_day_calendar_item.text = DateUtils.getDay3LettersName(date)*/

                calendarItemBinding.tvDateCalendarItem.text = DateUtils.getDayNumber(date)
                calendarItemBinding.tvDayCalendarItem.text = DateUtils.getDay3LettersName(date)

            }
        }


        // using calendar changes observer we can track changes in calendar
        val myCalendarChangesObserver = object : CalendarChangesObserver {
            // you can override more methods, in this example we need only this one
            override fun whenSelectionChanged(isSelected: Boolean, position: Int, date: Date) {

                calendarItemBinding.tvDateCalendarItem.text = DateUtils.getDayNumber(date)
                calendarItemBinding.tvDayCalendarItem.text = DateUtils.getDay3LettersName(date)

                binding.tvDate.text = "${DateUtils.getMonthName(date)}, ${DateUtils.getDayNumber(date)} "
                binding.tvDay.text = DateUtils.getDayName(date)
                super.whenSelectionChanged(isSelected, position, date)
            }
        }

        // selection manager is responsible for managing selection
        val mySelectionManager = object : CalendarSelectionManager {
            override fun canBeItemSelected(position: Int, date: Date): Boolean {
                // set date to calendar according to position
                val cal = Calendar.getInstance()
                cal.time = date
                // in this example sunday and saturday can't be selected, others can
                return when (cal[Calendar.DAY_OF_WEEK]) {
                    Calendar.SATURDAY -> false
                    Calendar.SUNDAY -> false
                    else -> true
                }
            }
        }

        // here we init our calendar, also you can set more properties if you haven't specified in XML layout
        val singleRowCalendar = binding.mainSingleRowCalendar.apply {
            calendarViewManager = myCalendarViewManager
            calendarChangesObserver = myCalendarChangesObserver
            calendarSelectionManager = mySelectionManager
            setDates(getFutureDatesOfCurrentMonth())
            init()
        }

        binding.btnRight.setOnClickListener {
            singleRowCalendar.setDates(getDatesOfNextMonth())
        }

        binding.btnLeft.setOnClickListener {
            singleRowCalendar.setDates(getDatesOfPreviousMonth())
        }

        return binding.root
    }


    private fun getDatesOfNextMonth(): List<Date> {
        currentMonth++ // + because we want next month
        if (currentMonth == 12) {
            // we will switch to january of next year, when we reach last month of year
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] + 1)
            currentMonth = 0 // 0 == january
        }
        return getDates(mutableListOf())
    }

    private fun getDatesOfPreviousMonth(): List<Date> {
        currentMonth-- // - because we want previous month
        if (currentMonth == -1) {
            // we will switch to december of previous year, when we reach first month of year
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] - 1)
            currentMonth = 11 // 11 == december
        }
        return getDates(mutableListOf())
    }

    private fun getFutureDatesOfCurrentMonth(): List<Date> {
        // get all next dates of current month
        currentMonth = calendar[Calendar.MONTH]
        return getDates(mutableListOf())
    }

    private fun getDates(list: MutableList<Date>): List<Date> {
        // load dates of whole month
        calendar.set(Calendar.MONTH, currentMonth)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        list.add(calendar.time)
        while (currentMonth == calendar[Calendar.MONTH]) {
            calendar.add(Calendar.DATE, +1)
            if (calendar[Calendar.MONTH] == currentMonth)
                list.add(calendar.time)
        }
        calendar.add(Calendar.DATE, -1)
        return list
    }

    override fun onClick(v: View) {
        when (v) {
            binding.btnDate -> {
                SingleDateAndTimePickerDialog.Builder(context)
                    .bottomSheet()
                    .curved()
                    .mustBeOnFuture()
                    .displayListener { picker ->
                        val date = picker.date
                        binding.inDate.setText("$date")
                    }
                    .title("Set a session")
                    .listener { date -> binding.inDate.setText("$date") }.display()
            }
            binding.setAppointment -> {
                when {
                    binding.inDate.text.isEmpty() -> {
                        Toast.makeText(
                            requireContext(),
                            "you must set a date to set the appointment",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    else -> {

                        val selectedDate = binding.inDate.text.toString().toLong()
                        val selectedTime = binding.inDate.text.toString().toLong()

                        val dateList = listOf(selectedDate)
                        val timeList = listOf(selectedTime)

                        fireStore.collection("PsyUsers").document(auth.currentUser!!.uid)
                            .also { ref ->
                                dateList.forEach {
                                    ref.update("availableDates.$it", timeList)
                                }

                                ref.get().addOnSuccessListener {
                                    val x = it.get("availableDates") as Map<*, *>
                                    Log.d(APPOINTMENTS_FRAGMENT_TAG, "onClick: $x")
                                }
                            }
                    }
                }
            }
        }
    }

}