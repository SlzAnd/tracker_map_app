package com.example.authentication.kotlin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class RegisterFragmentKt : BaseFragmentKt() {

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
        return "Sign up"
    }

    override fun getButtonText(): String {
        return "SIGN UP"
    }

    override fun getBottomNavigationText(): String {
        return "Already have an account?"
    }

    override fun getBottomNavigationLink(): String {
        return "Sign in"
    }

    override fun getForgotPasswordVisibility(): Int {
        return View.GONE
    }

    override fun onCLickButtonEvent(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    authenticationEvent.onSuccessRegistration()
                } else {
                    Toast.makeText(
                        context, "Authentication failed: " + task.result.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    override fun onClickLinkEvent() {
        authenticationEvent.onChangeToLoginScreen()
    }

}