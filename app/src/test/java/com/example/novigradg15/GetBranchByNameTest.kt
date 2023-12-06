package com.example.novigradg15

import junit.framework.TestCase
import org.junit.Test
import org.mockito.Mockito

class GetBranchByNameTest {

    @Test
    fun validBranchNameTest() {
        val welcomeActivity = Mockito.mock(PhotoVisitActivity::class.java)
        Mockito.`when`(welcomeActivity.getBranchByName("Maple Garden Services")).thenReturn(true)
        TestCase.assertEquals(true, welcomeActivity.getBranchByName("Maple Garden Services"))
    }

    @Test
    fun invalidBranchNameTest() {
        val welcomeActivity = Mockito.mock(PhotoVisitActivity::class.java)
        Mockito.`when`(welcomeActivity.getBranchByName("Invalid Branch Example")).thenReturn(true)
        TestCase.assertEquals(true, welcomeActivity.getBranchByName("Invalid Branch Example"))
    }

}