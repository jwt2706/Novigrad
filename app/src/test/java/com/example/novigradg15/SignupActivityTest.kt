package com.example.novigradg15

import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
class SignupActivityTest {
    @Test
    fun testBadSignupCreds() {
        val signup = mock(SignupActivity::class.java)
        assertEquals(false, signup.createAccount("coolman123", "bademailformat", "passwrd432", "G1", "Admin"))
    }
}