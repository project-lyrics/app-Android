package com.lyrics.feelin.navigation

import org.junit.Assert.assertEquals
import org.junit.Test

class FeelinDestinationTest {
    @Test
    fun editGenderBirthYearUsesDedicatedRoute() {
        assertEquals(
            "edit_gender_birth_year",
            FeelinDestination.EditGenderBirthYear.route,
        )
    }
}
