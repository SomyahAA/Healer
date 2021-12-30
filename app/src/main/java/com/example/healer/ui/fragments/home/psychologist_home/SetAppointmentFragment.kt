package com.example.healer.ui.fragments.home.psychologist_home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.app.TimePickerDialog
import android.app.DatePickerDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.example.healer.databinding.SetAppointmentFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*


private const val TAG = "SetAppointmentFragment"

class SetAppointmentFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: SetAppointmentFragmentBinding
    private val fireStore = Firebase.firestore
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SetAppointmentFragmentBinding.inflate(layoutInflater)

        binding.btnDate.setOnClickListener(this)
        binding.btnTime.setOnClickListener(this)
        binding.setAppointment.setOnClickListener(this)
        return binding.root
    }

    override fun onClick(v: View) {
        when (v) {
            binding.btnDate -> {
                // Get Current Date
                val c: Calendar = Calendar.getInstance()
                val mYear = c.get(Calendar.YEAR)
                val mMonth = c.get(Calendar.MONTH)
                val mDay = c.get(Calendar.DAY_OF_MONTH)

                DatePickerDialog(
                    requireContext(), { _, year, monthOfYear, dayOfMonth ->
                        binding.inDate.setText("$dayOfMonth${(monthOfYear + 1)}$year")
                    },
                    mYear, mMonth, mDay
                ).show()

                Log.d("n", " $mYear, $mMonth ,$mDay")
            }
            binding.btnTime -> {

                // Get Current Time
                val c: Calendar = Calendar.getInstance()
                val mHour = c.get(Calendar.HOUR_OF_DAY)
                val mMinute = c.get(Calendar.MINUTE)

                // Launch Time Picker Dialog
                val timePickerDialog = TimePickerDialog(
                    requireContext(),
                    { _, hourOfDay, minute -> binding.inTime.setText("$hourOfDay$minute") },
                    mHour,
                    mMinute,
                    false
                )
                timePickerDialog.show()
            }
            binding.setAppointment -> {
                when {
                    binding.inDate.text.isEmpty() -> {
                        Toast.makeText(
                            requireContext(),
                            "you must add a date to set the appointment",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    binding.inTime.text.isEmpty() -> {
                        Toast.makeText(
                            requireContext(),
                            "you must add a time to set the appointment",
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
                                    Log.d(TAG, "onClick: $x")
                                }
                            }
                    }
                }
            }
        }
    }
}


