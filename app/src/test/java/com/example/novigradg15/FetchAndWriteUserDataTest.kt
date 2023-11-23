package com.example.novigradg15
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class FetchAndWriteUserDataTest {

    @Test
    fun dbAccessTest() {
        val welcomeActivity = mock(WelcomeActivity::class.java)
        `when`(welcomeActivity.fetchAndWriteUserData()).thenReturn(true)
        assertEquals(true, welcomeActivity.fetchAndWriteUserData())
    }

}