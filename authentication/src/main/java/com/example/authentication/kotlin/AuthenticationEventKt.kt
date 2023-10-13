package com.example.authentication.kotlin

import com.google.firebase.auth.FirebaseAuth

abstract class AuthenticationEventKt {
    abstract fun onSuccessLogin()
    abstract fun onSuccessRegistration()
    abstract fun onChangeToLoginScreen()
    abstract fun onChangeToRegisterScreen()

    fun logOut() {
        FirebaseAuth.getInstance().signOut()
        onChangeToLoginScreen()
    }
}