package com.example.healer.ui.fragments.accounts.register


import com.example.healer.ui.fragments.accounts.register.psychologist_register.RegistrationUtil
import junit.framework.Assert.assertFalse
import org.junit.Test

class RegistrationUtilTest{

    @Test
    fun `empty email return false`(){
        val result = RegistrationUtil.validateRegistrationInput(
            "somyah",
            "0565656543",
            "s@gmail.com",
            "female",
            "1"
        )
        assertFalse(result)
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
        assertFalse(result)
    }

    @Test
    fun `password has less than five digits`(){
        val result = RegistrationUtil.validateRegistrationInput(
            "sss",
            "0565656543",
            "sss@gmail.com",
            "male",
            "12"
        )
        assertFalse(result)
    }

}