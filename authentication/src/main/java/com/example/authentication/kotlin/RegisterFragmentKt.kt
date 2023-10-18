package com.example.authentication.kotlin

import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class RegisterFragmentKt : BaseFragmentKt() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    companion object {
        private var authenticationEvent: AuthenticationEventKt? = null
        fun setAuthenticationEvent(event: AuthenticationEventKt) {
            authenticationEvent = event
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            authenticationEvent?.onSuccessLogin()
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
                    authenticationEvent?.onChangeToLoginScreen()
                } else {
                    Toast.makeText(
                        context, "Authentication failed: " + task.result.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    override fun onClickLinkEvent() {
        authenticationEvent?.onChangeToLoginScreen()
    }

}