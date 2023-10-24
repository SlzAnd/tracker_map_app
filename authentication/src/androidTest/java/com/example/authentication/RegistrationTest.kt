package com.example.authentication

import android.annotation.SuppressLint
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.authentication.kotlin.RegisterFragmentKt
import com.example.authentication.kotlin.initialAuthMockkSetup
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.startsWith
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@SuppressLint("CheckResult")
@RunWith(AndroidJUnit4::class)
class RegistrationTest {
    private var scenario: FragmentScenario<RegisterFragmentKt>? = null
    private val testUserEmail = "testUser@test.ua"
    private val testUserPassword = "password"

    private val failureTask: Task<AuthResult> = mockk()

    @Before
    fun setup() {
        initialAuthMockkSetup()
        every { failureTask.isSuccessful } returns false
        every { failureTask.result } returns mockk()

        val capturedListener = slot<OnCompleteListener<AuthResult>>()
        every { failureTask.addOnCompleteListener(capture(capturedListener)) } answers {
            val result = mockk<AuthResult>()
            every { result.user } returns mockk()
            capturedListener.captured.onComplete(failureTask)
            failureTask
        }

        every {
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(testUserEmail, testUserPassword)
        } returns failureTask

        scenario = launchFragmentInContainer(themeResId = R.style.Theme_Tracker_task)
        scenario?.moveToState(Lifecycle.State.STARTED)
    }

    @After
    fun tearDown() {
        scenario?.close()
    }

    @Test
    fun test_emptyEmailFieldNotValid() {
        onView(withId(R.id.user_name_edit_text)).perform(typeText(""))
        onView(withId(R.id.password_edit_text)).perform(typeText(testUserPassword))
        onView(withId(R.id.auth_button)).perform(click())

        assertThat(
            onView(withId(R.id.user_name_input_layout))
                .check(matches(hasErrorMessage("Error email. Email can't be empty")))
        )
    }

    @Test
    fun test_emptyPasswordFieldNotValid() {
        onView(withId(R.id.user_name_edit_text)).perform(typeText(testUserEmail))
        onView(withId(R.id.password_edit_text)).perform(typeText(""))
        onView(withId(R.id.auth_button)).perform(click())

        assertThat(
            onView(withId(R.id.password_input_layout))
                .check(matches(hasErrorMessage("Error password. Password can't be empty")))
        )
    }

    @Test
    fun test_passwordLessThanSixSymbolsNotValid() {
        onView(withId(R.id.user_name_edit_text)).perform(typeText(testUserEmail))
        onView(withId(R.id.password_edit_text)).perform(typeText("12345"))
        onView(withId(R.id.auth_button)).perform(click())

        assertThat(
            onView(withId(R.id.password_input_layout))
                .check(matches(hasErrorMessage("Error password. Password should be at least 6 symbols")))
        )
    }

    @Test
    fun test_notValidEmailOccursError() {
        onView(withId(R.id.user_name_edit_text)).perform(typeText("email.email"))
        onView(withId(R.id.password_edit_text)).perform(typeText(testUserPassword))
        onView(withId(R.id.auth_button)).perform(click())

        assertThat(
            onView(withId(R.id.user_name_input_layout))
                .check(matches(hasErrorMessage("Error email. Enter the correct email")))
        )
    }

    @Test
    fun test_successfulRegistrationCase() {
        onView(withId(R.id.user_name_edit_text)).perform(typeText(testUserEmail))
        onView(withId(R.id.password_edit_text)).perform(typeText(testUserPassword))
        onView(withId(R.id.password_edit_text)).perform(ViewActions.closeSoftKeyboard())
        onView(withId(R.id.auth_button)).perform(click())

        assertThat(
            onView(withId(R.id.user_name_input_layout))
                .check(matches(not(hasErrorMessage("Error email. Enter the correct email"))))
        )
        assertThat(
            onView(withId(R.id.password_input_layout))
                .check(matches(not(hasErrorMessage("Error password. Password should be at least 6 symbols"))))
        )
    }

    @Test
    fun test_failureRegistrationCase() {
        onView(withId(R.id.user_name_edit_text)).perform(typeText(testUserEmail))
        onView(withId(R.id.password_edit_text)).perform(typeText(testUserPassword))
        onView(withId(R.id.password_edit_text)).perform(ViewActions.closeSoftKeyboard())
        onView(withId(R.id.auth_button)).perform(click())

        onView(withText(startsWith("Authentication failed: ")))
            .inRoot(ToastMatcher.isToast())
            .check(matches(isDisplayed()))
    }


    @Test
    fun test_forgotPasswordMessageIsNotVisible() {
        assertThat(
            onView(withId(R.id.forgot_pass_text_view))
                .check(matches(not(isDisplayed())))
        )
    }

    private fun hasErrorMessage(expectedErrorMessage: String): InputErrorMatcher {
        return InputErrorMatcher(expectedErrorMessage)
    }
}