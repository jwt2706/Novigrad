package com.example.novigradg15

import com.google.firebase.firestore.FirebaseFirestore
import junit.framework.TestCase
import org.junit.Test
import org.mockito.Mockito

class FetchAndUpdateReviewsTest {

    @Test
    fun createRateDoc() {
        val activity = Mockito.mock(RateBranchActivity::class.java)
        Mockito.`when`(activity.fetchAndUpdateReviewsData()).thenReturn(true)
        TestCase.assertEquals(true, activity.fetchAndUpdateReviewsData())
    }


}