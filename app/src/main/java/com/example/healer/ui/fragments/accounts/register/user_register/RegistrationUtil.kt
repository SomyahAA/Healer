package com.example.healer.ui.fragments.accounts.register.user_register


object RegistrationUtil {



    // testing if email/password is empty
    // the password is less than 6 digit

    fun validateRegistrationInput(
        Name:String,
        PhoneNumber:String,
        email:String,
        gender:String,
        password:String
    ):Boolean{
        if (Name.isNotEmpty() || PhoneNumber.isNotEmpty()  ||email.isNotEmpty()  ||gender.isNotEmpty()  || password.isNotEmpty() ){
            return true
        }
        if (password.count{it.isDigit()} > 6 ){
            return true
        }
        return true
    }

}