package com.example.novigradg15
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class CreateServiceTest {

    @Test
    fun withAdminAccess() {
        val addService = mock(AddServiceActivity::class.java)
        `when`(addService.createService("lM2HZlZcJefwWmkqSpi6CO0NTW73")).thenReturn(true)
        assertEquals(true, addService.createService("lM2HZlZcJefwWmkqSpi6CO0NTW73"))
    }

    @Test
    fun withoutAdminAccess() {
        val addService = mock(AddServiceActivity::class.java)
        `when`(addService.createService("fakeid")).thenReturn(false)
        assertEquals(false, addService.createService("fakeid"))
    }

}