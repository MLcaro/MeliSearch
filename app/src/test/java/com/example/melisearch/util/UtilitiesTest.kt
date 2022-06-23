package com.example.melisearch.util

import org.junit.Test
import org.junit.jupiter.api.Assertions.*

internal class UtilitiesTest {

    @Test
    fun checkInputTest() {
        //given
        val testInput = ".PL9"
        val expectedValue = false
        //when
        val result = Utilities.checkInput(testInput)
        //then
        assertEquals(expectedValue, result)

    }
}