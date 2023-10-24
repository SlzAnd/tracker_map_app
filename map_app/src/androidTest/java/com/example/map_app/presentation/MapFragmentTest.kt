package com.example.map_app.presentation

import android.annotation.SuppressLint
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import com.example.authentication.kotlin.initialAuthMockkSetup
import com.example.map_app.R
import com.example.map_app.kotlin.presentation.MapFragmentKt
import com.example.map_app.launchFragmentInHiltContainer
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@SuppressLint("CheckResult")
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@HiltAndroidTest
class MapFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)
    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    @Before
    fun setup() {
        initialAuthMockkSetup()
        hiltRule.inject()
        launchFragmentInHiltContainer<MapFragmentKt>()
    }

    @Test
    fun test_correctToolbarTitleAndSelectedDateMessage() {
        assertThat(
            onView(withId(R.id.toolbar_title)).check(matches(withText(R.string.timeline)))
        )
        assertThat(
            onView(withId(R.id.time_text_view)).check(matches(withText(R.string.last_24_h)))
        )
    }

    @Test
    fun test_openCalendarDialog() {
        onView(withId(R.id.toolbar_calendar_icon)).perform(click())
        assertThat(
            onView(withId(R.id.calendar_dialog)).check(matches(isDisplayed()))
        )
    }

    @Test
    fun test_selectedCorrectDateDisplayOnTheBottomMessage() {
        onView(withId(R.id.toolbar_calendar_icon)).perform(click())
        val selectedDate = device.findObject(By.text("5"))
        selectedDate.click()
        val okButton = device.findObject(By.text("Ok"))
        okButton.click()
        assertThat(
            onView(withId(R.id.time_text_view)).check(matches(withText("October 5, 2023")))
        )
    }

    @Test
    fun test_selectingFutureDateImpossible() {
        onView(withId(R.id.toolbar_calendar_icon)).perform(click())
        val selectedDate = device.findObject(By.text("27"))
        selectedDate.click()
        val okButton = device.findObject(By.text("Ok"))
        okButton.click()

        //calendar dialog still in the screen for changing the date(after click on the OK button)
        assertThat(
            onView(withId(R.id.calendar_dialog)).check(matches(isDisplayed()))
        )
    }
}