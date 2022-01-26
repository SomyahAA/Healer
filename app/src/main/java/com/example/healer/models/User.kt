package com.example.healer.models

data class User(
    var name: String =" ",
    var phoneNumber: String="",
    var email: String = " ",
    var gender: String =" ",
    var profileImage: String ="",
    var myBookedAppointments: List <Appointment> = listOf()
)