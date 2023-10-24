package com.example.tracker_task.presentation

import android.Manifest
import android.annotation.SuppressLint
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.example.authentication.kotlin.initialAuthMockkSetup
import com.example.tracker_task.R
import com.example.tracker_task.kotlin.presentation.TrackerFragmentKt
import com.example.tracker_task.launchFragmentInHiltContainer
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@SuppressLint("CheckResult")
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class SuccessTrackingScreenTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        initialAuthMockkSetup()
        hiltRule.inject()
        launchFragmentInHiltContainer<TrackerFragmentKt>()
    }

    @Test
    fun test_correctToolbarTitleForTrackerModule() {
        assertThat(
            onView(withId(R.id.toolbar_title)).check(matches(withText(R.string.tracker)))
        )
    }

    @Test
    fun test_startTrackingChangesUI() {
        assertThat(
            onView(withId(R.id.start_stop_button)).check(matches(withText(R.string.start_button_text)))
        )
        onView(withId(R.id.start_stop_button)).perform(click())
        assertThat(
            onView(withId(R.id.start_stop_button)).check(matches(withText(R.string.stop_button_text)))
        )
        assertThat(
            onView(withId(R.id.tracker_off_include)).check(matches(not(isDisplayed())))
        )
        assertThat(
            onView(withId(R.id.tracker_working_include)).check(matches(isDisplayed()))
        )
    }

    // Next test works only after the previous one
    @Test
    fun test_stopTrackingChangesUI() {
        // still collecting locations( after previous test ) and reload the app
        assertThat(
            onView(withId(R.id.start_stop_button)).check(matches(withText(R.string.stop_button_text)))
        )
        onView(withId(R.id.start_stop_button)).perform(click())
        assertThat(
            onView(withId(R.id.start_stop_button)).check(matches(withText(R.string.start_button_text)))
        )
        assertThat(
            onView(withId(R.id.tracker_off_include)).check(matches(isDisplayed()))
        )
        assertThat(
            onView(withId(R.id.tracker_working_include)).check(matches(not(isDisplayed())))
        )
    }
}