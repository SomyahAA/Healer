package com.example.healer.ui.fragments.accounts.register


import com.example.healer.ui.fragments.accounts.register.user_register.RegistrationUtil
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RegistrationUtilTest{

    @Test
    fun `empty email return false`(){
        val result = RegistrationUtil.validateRegistrationInput(
            "somyah",
            "0565656543",
            "",
            "female",
            "1"
        )
        assertThat(result).isFalse()
    }
    @Test
    fun `empty password return false`(){
        val result = RegistrationUtil.validateRegistrationInput(
            "somyah",
            "0565656543",
            "sss@gmail.com",
            "male",
            ""
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `password has less than six digits`(){
        val result = RegistrationUtil.validateRegistrationInput(
            "sss@gmail.com",
            "0565656543",
            "sss@gmail.com",
            "male",
            "15656588"
        )
        assertThat(result).isFalse()
    }

}