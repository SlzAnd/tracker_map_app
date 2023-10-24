package com.example.authentication

import android.annotation.SuppressLint
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.authentication.kotlin.LoginFragmentKt
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import com.example.authentication.ToastMatcher.Companion.isToast
import com.example.authentication.ToastMatcher.Companion.onToast
import com.example.authentication.kotlin.initialAuthMockkSetup


@SuppressLint("CheckResult")
@RunWith(AndroidJUnit4::class)
class LoginTest {

    private var scenario: FragmentScenario<LoginFragmentKt>? = null
    private val testUserEmail = "testUser@test.ua"
    private val testUserPassword = "password"
    private val wrongPassword = "wrongPass"

    private val successTask: Task<AuthResult> = mockk()
    private val failureTask: Task<AuthResult> = mockk()

    @Before
    fun setup() {
        initialAuthMockkSetup()
        every { successTask.isSuccessful } returns true
        every { failureTask.isSuccessful } returns false

        val capturedListener = slot<OnCompleteListener<AuthResult>>()
        every { successTask.addOnCompleteListener(capture(capturedListener)) } answers {
            // Simulate the behavior of the listener when the task is successful
            val result = mockk<AuthResult>()
            every { result.user } returns mockk()
            capturedListener.captured.onComplete(successTask)
            successTask
        }

        every { failureTask.addOnCompleteListener(capture(capturedListener)) } answers {
            // Simulate the behavior of the listener when the task is not successful
            val result = mockk<AuthResult>()
            every { result.user } returns mockk()
            capturedListener.captured.onComplete(failureTask)
            failureTask
        }

        every {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(testUserEmail, testUserPassword)
        } returns successTask

        every {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(testUserEmail, wrongPassword)
        } returns failureTask

        scenario = launchFragmentInContainer(themeResId = R.style.Theme_Tracker_task)
        scenario?.moveToState(Lifecycle.State.STARTED)
    }

    @After
    fun tearDown() {
        scenario?.close()
    }

    @Test
    fun test_forgotPasswordMessageIsVisible() {
        assertThat(
            onView(withId(R.id.forgot_pass_text_view))
                .check(matches(isDisplayed()))
        )
    }

    @Test
    fun test_successfulLoginCase() {
        onView(withId(R.id.user_name_edit_text)).perform(typeText(testUserEmail))
        onView(withId(R.id.password_edit_text)).perform(typeText(testUserPassword))
        onView(withId(R.id.password_edit_text)).perform(closeSoftKeyboard())
        onView(withId(R.id.auth_button)).perform(ViewActions.click())
        onView(withText("Wrong email or password!"))
            .inRoot(isToast())
            .check(doesNotExist())
    }

    @Test
    fun test_failureLoginCase_showsToast() {
        onView(withId(R.id.user_name_edit_text)).perform(typeText(testUserEmail))
        onView(withId(R.id.password_edit_text)).perform(typeText(wrongPassword))
        onView(withId(R.id.password_edit_text)).perform(closeSoftKeyboard())
        onView(withId(R.id.auth_button)).perform(ViewActions.click())
        onToast("Wrong email or password!")
            .check(matches(isDisplayed()))
    }
}