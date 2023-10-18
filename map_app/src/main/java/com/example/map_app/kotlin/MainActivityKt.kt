package com.example.map_app.kotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.example.authentication.kotlin.AuthenticationEventKt
import com.example.authentication.kotlin.LoginFragmentKt
import com.example.authentication.kotlin.RegisterFragmentKt
import com.example.map_app.R
import com.example.map_app.databinding.ActivityMainBinding
import com.example.map_app.kotlin.presentation.MapFragmentKt
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivityKt : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private var authenticationEvent: AuthenticationEventKt? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        authenticationEvent = object : AuthenticationEventKt {
            override fun onSuccessLogin() {
                Navigation.findNavController(binding!!.fragmentContainerView)
                    .navigate(R.id.action_loginFragmentKt_to_mapFragmentKt)
            }

            override fun onSuccessRegistration() {
                Navigation.findNavController(binding!!.fragmentContainerView)
                    .navigate(R.id.action_registerFragmentKt_to_loginFragmentKt)
            }

            override fun onChangeToLoginScreen() {
                Navigation.findNavController(binding!!.fragmentContainerView)
                    .navigate(R.id.action_registerFragmentKt_to_loginFragmentKt)
            }

            override fun onChangeToRegisterScreen() {
                Navigation.findNavController(binding!!.fragmentContainerView)
                    .navigate(R.id.action_loginFragmentKt_to_registerFragmentKt)
            }

            override fun onLogOut() {
                Navigation.findNavController(binding!!.fragmentContainerView)
                    .navigate(R.id.action_mapFragmentKt_to_loginFragmentKt)
            }
        }

        LoginFragmentKt.setAuthenticationEvent(authenticationEvent as AuthenticationEventKt)
        RegisterFragmentKt.setAuthenticationEvent(authenticationEvent as AuthenticationEventKt)
        MapFragmentKt.setAuthenticationEvent(authenticationEvent as AuthenticationEventKt)
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}