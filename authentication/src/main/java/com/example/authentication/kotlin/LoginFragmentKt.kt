package com.example.authentication.kotlin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class LoginFragmentKt: BaseFragmentKt() {

    private lateinit var mAuth: FirebaseAuth
    lateinit var authenticationEvent: AuthenticationEventKt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            authenticationEvent.onSuccessLogin()
        }
    }


    override fun getTitle(): String {
        return "Sign in"
    }

    override fun getButtonText(): String {
        return "SIGN IN"
    }

    override fun getBottomNavigationText(): String {
        return "Don't have an account?"
    }

    override fun getBottomNavigationLink(): String {
        return "Sign Up"
    }

    override fun getForgotPasswordVisibility(): Int {
        return View.VISIBLE
    }

    override fun onCLickButtonEvent(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    authenticationEvent.onSuccessLogin()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Wrong email or password!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    override fun onClickLinkEvent() {
        authenticationEvent.onChangeToRegisterScreen()
    }
}