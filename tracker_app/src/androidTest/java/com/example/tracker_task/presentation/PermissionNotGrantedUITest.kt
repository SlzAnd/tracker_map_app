package com.example.tracker_task.presentation

import android.annotation.SuppressLint
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.authentication.kotlin.initialAuthMockkSetup
import com.example.tracker_task.R
import com.example.tracker_task.kotlin.presentation.TrackerFragmentKt
import com.example.tracker_task.launchFragmentInHiltContainer
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/*
    WARNING: After running this test you should manually deny location permission!
*/

@SuppressLint("CheckResult")
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class PermissionNotGrantedUITest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        initialAuthMockkSetup()
        hiltRule.inject()
        launchFragmentInHiltContainer<TrackerFragmentKt> {}
    }

    @Test
    fun test_locationPermissionDeniedUI() {
        assertThat(
            onView(withId(R.id.start_stop_button))
                .check(matches(withText(R.string.start_button_text)))
        )
        assertThat(
            onView(withId(R.id.tracker_off_include))
                .check(matches(isDisplayed()))
        )
        assertThat(
            onView(withId(R.id.permission_message))
                .check(matches(isDisplayed()))
        )
    }
}