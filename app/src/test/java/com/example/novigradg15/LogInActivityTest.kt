package com.example.novigradg15

import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock

class LogInActivityTest {
    @Test
    fun testBadLogInCreds() {
        val logInActivity = mock(MainActivity::class.java)
        assertEquals(false, logInActivity.logIn("missingAtInEmail.com", "7"))
    }

}