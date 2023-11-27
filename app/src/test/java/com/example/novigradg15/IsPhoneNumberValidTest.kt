package com.example.novigradg15

import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
class IsPhoneNumberValidTest {
    
    @Test
    fun validPhoneNumberTest() {
        val activity = mock(EmployeeModifyBranchActivity::class.java)
        `when`(activity.isPhoneNumberValid("6131234567")).thenReturn(true)
        assertEquals(true, activity.isPhoneNumberValid("6131234567"))
    }

    @Test
    fun lettersPhoneNumberTest() {
        val activity = mock(EmployeeModifyBranchActivity::class.java)
        `when`(activity.isPhoneNumberValid("letters532")).thenReturn(false)
        assertEquals(false, activity.isPhoneNumberValid("letters532"))
    }

    @Test
    fun notEnoughDigitsPhoneNumberTest() {
        val activity = mock(EmployeeModifyBranchActivity::class.java)
        `when`(activity.isPhoneNumberValid("12")).thenReturn(false)
        assertEquals(false, activity.isPhoneNumberValid("12"))
    }
}