package com.example.map_app.kotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.authentication.kotlin.AuthenticationEventKt
import com.example.authentication.kotlin.LoginFragmentKt
import com.example.authentication.kotlin.RegisterFragmentKt
import com.example.map_app.databinding.ActivityMainBinding
import com.example.map_app.kotlin.presentation.MapFragmentKt
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivityKt : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private lateinit var authenticationEvent: AuthenticationEventKt


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        // fragments
        val loginFragment = LoginFragmentKt()
        val registerFragment = RegisterFragmentKt()
        val mapFragment = MapFragmentKt()

        val fragmentManager = supportFragmentManager

        authenticationEvent = object : AuthenticationEventKt() {
            override fun onSuccessLogin() {
                val transaction: FragmentTransaction = fragmentManager.beginTransaction()
                transaction.replace(binding!!.mapsFragmentContainer.id, mapFragment)
                transaction.commit()
            }

            override fun onSuccessRegistration() {
                val transaction: FragmentTransaction = fragmentManager.beginTransaction()
                transaction.replace(binding!!.mapsFragmentContainer.id, loginFragment)
                transaction.commit()
            }

            override fun onChangeToLoginScreen() {
                val transaction: FragmentTransaction = fragmentManager.beginTransaction()
                transaction.replace(binding!!.mapsFragmentContainer.id, loginFragment)
                transaction.commit()
            }

            override fun onChangeToRegisterScreen() {
                val transaction: FragmentTransaction = fragmentManager.beginTransaction()
                transaction.replace(binding!!.mapsFragmentContainer.id, registerFragment)
                transaction.commit()
            }
        }

        loginFragment.authenticationEvent = authenticationEvent
        registerFragment.authenticationEvent = authenticationEvent
        mapFragment.authenticationEvent = authenticationEvent

        // first-viewed fragment - Login
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(binding!!.mapsFragmentContainer.id, loginFragment)
        transaction.commit()
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}