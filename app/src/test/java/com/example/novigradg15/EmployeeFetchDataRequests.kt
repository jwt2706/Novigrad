package com.example.novigradg15

import junit.framework.TestCase
import org.junit.Test
import org.mockito.Mockito


class EmployeeFetchDataRequests {

    @Test
    fun fetchData() {
        val activity = Mockito.mock(EmployeeWelcomeBranchActivity::class.java)
        Mockito.`when`(activity.fetchAndWriteRequestsData()).thenReturn(true)
        TestCase.assertEquals(true, activity.fetchAndWriteRequestsData())
    }
}