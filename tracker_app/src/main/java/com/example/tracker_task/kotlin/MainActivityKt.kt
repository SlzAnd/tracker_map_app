package com.example.tracker_task.kotlin

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.example.authentication.kotlin.AuthenticationEventKt
import com.example.authentication.kotlin.LoginFragmentKt
import com.example.authentication.kotlin.RegisterFragmentKt
import com.example.tracker_task.R
import com.example.tracker_task.databinding.ActivityMainBinding
import com.example.tracker_task.kotlin.presentation.TrackerEventKt
import com.example.tracker_task.kotlin.presentation.TrackerFragmentKt
import com.example.tracker_task.kotlin.presentation.TrackerViewModelKt
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivityKt : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: TrackerViewModelKt by viewModels()
    private var authenticationEvent: AuthenticationEventKt? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authenticationEvent = object : AuthenticationEventKt {
            override fun onSuccessLogin() {
                Navigation.findNavController(binding.fragmentContainerView)
                    .navigate(R.id.action_loginFragmentKt_to_trackerFragmentKt)
            }

            override fun onSuccessRegistration() {
                Navigation.findNavController(binding.fragmentContainerView)
                    .navigate(R.id.action_registerFragmentKt_to_loginFragmentKt)
            }

            override fun onChangeToLoginScreen() {
                Navigation.findNavController(binding.fragmentContainerView)
                    .navigate(R.id.action_registerFragmentKt_to_loginFragmentKt)
            }

            override fun onChangeToRegisterScreen() {
                Navigation.findNavController(binding.fragmentContainerView)
                    .navigate(R.id.action_loginFragmentKt_to_registerFragmentKt)
            }

            override fun onLogOut() {
                Navigation.findNavController(binding.fragmentContainerView)
                    .navigate(R.id.logOutNavigateToLogin)
            }
        }

        LoginFragmentKt.setAuthenticationEvent(authenticationEvent as AuthenticationEventKt)
        RegisterFragmentKt.setAuthenticationEvent(authenticationEvent as AuthenticationEventKt)
        TrackerFragmentKt.setAuthenticationEvent(authenticationEvent as AuthenticationEventKt)
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