package com.example.authentication

import android.view.View
import androidx.test.espresso.matcher.BoundedMatcher
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description

class InputErrorMatcher(private val expectedErrorMessage: String) :
    BoundedMatcher<View?, TextInputLayout?>(TextInputLayout::class.java) {
    override fun matchesSafely(item: TextInputLayout?): Boolean {
        if (item == null) {
            return false
        }
        val error = item.error ?: return false
        val hint = error.toString()
        return expectedErrorMessage == hint
    }

    override fun describeTo(description: Description) {
        description.appendText("has error message: ")
        description.appendValue(expectedErrorMessage)
    }
}
