package com.example.tracker_task.presentation

import android.Manifest
import android.annotation.SuppressLint
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
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

/*
    WARNING: Before running this test make sure you turned off the device location (GPS)!
*/
@SuppressLint("CheckResult")
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class TurnOffDeviceLocationTest {

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
    fun test_GpsOffUI() {
        assertThat(
            onView(withId(R.id.start_stop_button))
                .check(matches(withText(R.string.start_button_text)))
        )
        assertThat(
            onView(withId(R.id.tracker_off_include))
                .check(matches(not(isDisplayed())))
        )
        assertThat(
            onView(withId(R.id.tracker_gps_off_include))
                .check(matches(isDisplayed()))
        )
        // check that click do nothing -> you can't run tracking locations when GPS was turned off
        onView(withId(R.id.start_stop_button)).perform(ViewActions.click())
        assertThat(
            onView(withId(R.id.start_stop_button))
                .check(matches(withText(R.string.start_button_text)))
        )
        assertThat(
            onView(withId(R.id.tracker_off_include))
                .check(matches(not(isDisplayed())))
        )
        assertThat(
            onView(withId(R.id.tracker_gps_off_include))
                .check(matches(isDisplayed()))
        )
    }
}