package com.example.healer.models


data class Psychologist(
    var name: String = "",
    var phoneNumber: String ="",
    var email: String="",
    var specialty: String = "", //List<String> listOf()
    var experienceYears: String ="",
    var bio: String ="",
    var availableDates : Map<Long,Array<Long>> = mapOf(),
    var profileImage: String ="@drawable/",
//    var id :String =""
)