package com.example.map_app.presentation

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.example.authentication.kotlin.initialAuthMockkSetup
import com.example.map_app.R
import com.example.map_app.kotlin.MainActivityKt
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@MediumTest
class NavigationMapFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private val navController: NavController = mockk { NavController::class.java }

    private lateinit var scenario: ActivityScenario<MainActivityKt>

    @Before
    fun setUp() {
        initialAuthMockkSetup()
        hiltRule.inject()

        every {
            navController.navigate(R.id.action_mapFragmentKt_to_loginFragmentKt)
        } returns mockk()

        scenario = ActivityScenario.launch(MainActivityKt::class.java).onActivity {
            Navigation.setViewNavController(
                it.requireViewById(R.id.fragmentContainerView),
                navController
            )
        }
    }

    @Test
    fun test_clickExitButtonNavigateToLoginScreen() {
        onView(withId(R.id.toolbar_exit_icon)).perform(click())

        verify {
            navController.navigate(R.id.action_mapFragmentKt_to_loginFragmentKt)
        }
    }
}