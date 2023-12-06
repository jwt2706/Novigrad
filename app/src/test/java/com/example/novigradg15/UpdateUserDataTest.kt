package com.example.novigradg15

import junit.framework.TestCase
import org.junit.Test
import org.mockito.Mockito

class UpdateUserDataTest {

    @Test
    fun fetchData() {
        val activity = Mockito.mock(EmployeeWelcomeBranchActivity::class.java)
        Mockito.`when`(activity.fetchAndWriteUserData()).thenReturn(true)
        TestCase.assertEquals(true, activity.fetchAndWriteUserData())
    }

}