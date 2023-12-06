package com.example.novigradg15
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class IsAdminTest {

    @Test
    fun isAdminOnAdminTest() {
        val welcomeActivity = mock(AdminWelcomeActivity::class.java)
        `when`(welcomeActivity.isAdmin()).thenReturn(true)
        assertEquals(true, welcomeActivity.isAdmin())
    }
}