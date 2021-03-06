package com.example.healer.ui.fragments.accounts.register.user_register


object RegistrationUtil {


    // testing if email/password is empty
    // the password is less than 6 digit

    fun validateRegistrationInput(
        Name: String,
        PhoneNumber: String,
        email: String,
        gender: String,
        password: String
    ): Boolean {
        if (Name.isEmpty() || PhoneNumber.isEmpty() || email.isEmpty() || gender.isEmpty() || password.isEmpty()) {
            return true
        } else if (password.count { it.isDigit() } < 5) {
            return true
        }
        return false
    }

}