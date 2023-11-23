package com.example.novigradg15

import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock

class BranchGettersTests {
    val branchActivity = mock(BranchCustomListAdapter::class.java)
    val data = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    val branch = BranchSettingsActivity()
    @Test
    fun getCountTest() {
        assertEquals(9, branchActivity.getCount())
    }

    @Test
    fun getItemTest() {
        assertEquals(3, branchActivity.getItem(3))
    }

    @Test
    fun getItemIdTest() {
        assertEquals(3, branchActivity.getItemId(3))
    }

}