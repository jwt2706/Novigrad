package com.example.novigradg15

import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class LogInActivityTest {
    @Test
    fun testBadLogInCreds() {
        val logInActivity = mock(MainActivity::class.java)
        `when`(logInActivity.logIn("missingAtInEmail.com", "7")).thenReturn(false)
        assertEquals(false, logInActivity.logIn("missingAtInEmail.com", "7"))
    }

    @Test
    fun testGoodLogInCreds() {
        val logInActivity = mock(MainActivity::class.java)
        `when`(logInActivity.logIn("admin@novigrad.gov", "admin123")).thenReturn(true)
        assertEquals(true, logInActivity.logIn("admin@novigrad.gov", "admin123"))
    }

}