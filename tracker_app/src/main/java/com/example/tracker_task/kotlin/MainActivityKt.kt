package com.example.tracker_task.kotlin

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.authentication.kotlin.AuthenticationEventKt
import com.example.authentication.kotlin.LoginFragmentKt
import com.example.authentication.kotlin.RegisterFragmentKt
import com.example.tracker_task.databinding.ActivityMainBinding
import com.example.tracker_task.kotlin.presentation.TrackerEventKt
import com.example.tracker_task.kotlin.presentation.TrackerFragmentKt
import com.example.tracker_task.kotlin.presentation.TrackerViewModelKt
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivityKt : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: TrackerViewModelKt by viewModels()
    private lateinit var authenticationEvent: AuthenticationEventKt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // fragments
        val loginFragment = LoginFragmentKt()
        val registerFragment = RegisterFragmentKt()
        val trackerFragment = TrackerFragmentKt()

        val fragmentManager = supportFragmentManager

        authenticationEvent = object : AuthenticationEventKt() {
            override fun onSuccessLogin() {
                val transaction: FragmentTransaction = fragmentManager.beginTransaction()
                transaction.replace(binding.trackerFragmentContainer.id, trackerFragment)
                transaction.commit()
            }

            override fun onSuccessRegistration() {
                val transaction: FragmentTransaction = fragmentManager.beginTransaction()
                transaction.replace(binding.trackerFragmentContainer.id, loginFragment)
                transaction.commit()
            }

            override fun onChangeToLoginScreen() {
                val transaction: FragmentTransaction = fragmentManager.beginTransaction()
                transaction.replace(binding.trackerFragmentContainer.id, loginFragment)
                transaction.commit()
            }

            override fun onChangeToRegisterScreen() {
                val transaction: FragmentTransaction = fragmentManager.beginTransaction()
                transaction.replace(binding.trackerFragmentContainer.id, registerFragment)
                transaction.commit()
            }
        }

        loginFragment.authenticationEvent = authenticationEvent
        registerFragment.authenticationEvent = authenticationEvent
        trackerFragment.authenticationEvent = authenticationEvent

        // first-viewed fragment - Login
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(binding.trackerFragmentContainer.id, loginFragment)
        transaction.commit()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty()) {
            if (requestCode == 0) {
                viewModel.onEvent(
                    TrackerEventKt.SetPermissionState(
                        grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                                grantResults[1] == PackageManager.PERMISSION_GRANTED
                    )
                )
            }
        }
    }
}