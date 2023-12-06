package com.example.novigradg15

import junit.framework.TestCase
import org.junit.Test
import org.mockito.Mockito

class WriteBranchData {

    @Test
    fun writeValidData() {
        val activity = Mockito.mock(EmployeeWelcomeBranchActivity::class.java)
        Mockito.`when`(activity.fetchAndWriteBranchData("g44Ca8ujQANlzMEjfNiyILCB1rZ2")).thenReturn(true)
        TestCase.assertEquals(true, activity.fetchAndWriteBranchData("g44Ca8ujQANlzMEjfNiyILCB1rZ2"))
    }

    @Test
    fun writeInvalidData() {
        val activity = Mockito.mock(EmployeeWelcomeBranchActivity::class.java)
        Mockito.`when`(activity.fetchAndWriteBranchData("invalidUID")).thenReturn(false)
        TestCase.assertEquals(false, activity.fetchAndWriteBranchData("invalidUID"))
    }

}