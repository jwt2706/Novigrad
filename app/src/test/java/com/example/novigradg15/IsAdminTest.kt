package com.example.novigradg15
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class IsAdminTest {

    @Test
    fun isAdminTest() {
        val welcomeActivity = mock(ClientWelcomeActivity::class.java)
        `when`(welcomeActivity.isAdmin("Admin")).thenReturn(true)
        assertEquals(true, welcomeActivity.isAdmin("Admin"))
    }

    @Test
    fun isCustomerTest() {
        val welcomeActivity = mock(ClientWelcomeActivity::class.java)
        `when`(welcomeActivity.isAdmin("Customer")).thenReturn(false)
        assertEquals(false, welcomeActivity.isAdmin("Customer"))
    }

    @Test
    fun isEmployeeTest() {
        val welcomeActivity = mock(ClientWelcomeActivity::class.java)
        `when`(welcomeActivity.isAdmin("Employee")).thenReturn(false)
        assertEquals(false, welcomeActivity.isAdmin("Employee"))
    }
}