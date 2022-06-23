package com.example.melisearch.util

import org.junit.Test
import org.junit.jupiter.api.Assertions.*

internal class UtilitiesTest {

    @Test
    fun checkInputWithNumberTest() {
        //given
        val testInput = ".PL9"
        val expectedValue = false
        //when
        val result = Utilities.checkInput(testInput)
        //then
        assertEquals(expectedValue, result)

    }

    @Test
    fun checkInputWithSpaceTest() {
        //given
        val testInput = "casa de campo"
        //when
        val result = Utilities.checkInput(testInput)
        //then
        assertTrue(result)
    }

    @Test
    fun checkInputEmptyTest() {
        //given
        val testInput = " "
        //when
        val result = Utilities.checkInput(testInput)
        //then
        assertFalse(result)

    }

    @Test
    fun checkInputToShortTest() {
        //given
        val testInput = "sa"
        //when
        val result = Utilities.checkInput(testInput)
        //then
        assertFalse(result)

    }
}