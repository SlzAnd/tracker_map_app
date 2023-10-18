package com.example.authentication.kotlin

import com.google.firebase.auth.FirebaseAuth

interface AuthenticationEventKt {

    fun onSuccessLogin()
    fun onSuccessRegistration()
    fun onChangeToLoginScreen()
    fun onChangeToRegisterScreen()
    fun onLogOut()

    fun logOut() {
        FirebaseAuth.getInstance().signOut()
        onLogOut()
    }
}