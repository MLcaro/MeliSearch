package com.example.melisearch.util

class Utilities {
companion object {
    /**this function check if the input text for search a category is longer than 2 characters
     * and all the characters are letters
     *  */
    fun checkInput(input: String): Boolean {
        var check: Boolean = false
        if (input.isNotEmpty()) {
            if (input.length >= 3) {
                check = input.all { it.isLetter() }
            }
        }
        return check
    }
}
}