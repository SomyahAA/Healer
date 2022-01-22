package com.example.healer.models

data class Psychologist(
var name: String = "",
var phoneNumber: String ="",
var email: String="",
var specialty: String = "",
var experienceYears: String ="",
var bio: String ="",
var profileImage: String ="",
var availableAppointments : MutableList<Appointment> = mutableListOf(),
val bookedAppointments:MutableList<Appointment> = mutableListOf(),
var id :String=""
)