package com.example.novigradg15
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
class isAdminTest {

    @Test
    fun isAdminTest() {
        val welcomeActivity = mock(WelcomeActivity::class.java)
        assertEquals(true, welcomeActivity.isAdmin("Admin"))
        //assertEquals(true, "Admin"=="Admin")
    }

    @Test
    fun isCustomerTest() {
        val welcomeActivity = mock(WelcomeActivity::class.java)
        assertEquals(false, welcomeActivity.isAdmin("Customer"))
    }

    @Test
    fun isEmployeeTest() {
        val welcomeActivity = mock(WelcomeActivity::class.java)
        assertEquals(false, welcomeActivity.isAdmin("Employee"))
    }
}