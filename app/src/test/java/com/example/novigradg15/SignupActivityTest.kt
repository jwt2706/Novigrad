package com.example.novigradg15

import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
class SignupActivityTest {
    @Test
    fun testBadSignupCreds() {
        val signupActivity = mock(SignupActivity::class.java)
        `when`(signupActivity.createAccount("coolman123", "badEmailFormat.com", "passwrd432", "G1", "Admin")).thenReturn(false)
        assertEquals(false, signupActivity.createAccount("coolman123", "badEmailFormat.com", "passwrd432", "G1", "Admin"))
    }

    @Test
    fun testGoodSignupCreds() {
        val signupActivity = mock(SignupActivity::class.java)
        `when`(signupActivity.createAccount("user564", "user@novigrad.gov", "passwrd432", "G1", "Customer")).thenReturn(true)
        assertEquals(true, signupActivity.createAccount("user564", "user@novigrad.gov", "passwrd432", "G1", "Customer"))
    }
}