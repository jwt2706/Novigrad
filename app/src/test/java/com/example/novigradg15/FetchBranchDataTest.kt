package com.example.novigradg15
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class FetchBranchDataTest {

    @Test
    fun fetchRealBranch() {
        val activity = mock(EmployeeWelcomeBranchActivity::class.java)
        `when`(activity.fetchAndWriteBranchData("q34RuFd9fIi7iENE2pe09E")).thenReturn(true)
        assertEquals(true, activity.fetchAndWriteBranchData("q34RuFd9fIi7iENE2pe09E"))
    }

    @Test
    fun fetchFakeBranch() {
        val activity = mock(EmployeeWelcomeBranchActivity::class.java)
        `when`(activity.fetchAndWriteBranchData("fakeUserId")).thenReturn(false)
        assertEquals(false, activity.fetchAndWriteBranchData("fakeUserId"))
    }
}