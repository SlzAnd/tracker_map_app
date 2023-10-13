package com.example.authentication.kotlin

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.authentication.databinding.FragmentBaseBinding
import java.util.regex.Pattern

abstract class BaseFragmentKt: Fragment() {

    private var binding: FragmentBaseBinding? = null
    abstract fun getTitle(): String
    protected abstract fun getButtonText(): String

    protected abstract fun getBottomNavigationText(): String

    protected abstract fun getBottomNavigationLink(): String

    protected abstract fun getForgotPasswordVisibility(): Int

    protected abstract fun onCLickButtonEvent(email: String, password: String)

    protected abstract fun onClickLinkEvent()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseBinding.inflate(inflater, container, false)
        Log.d("TAG", ""+binding!!.root)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // widgets
        val emailEditText = binding!!.userNameEditText
        val passwordEditText = binding!!.passwordEditText
        val emailInputLayout = binding!!.userNameInputLayout
        val passwordInputLayout = binding!!.passwordInputLayout
        val authButton: Button = binding!!.authButton
        val bottomNavLink = binding!!.bottomNavLink
        val toolbarTitle = binding!!.toolbarTitle

        toolbarTitle.text = getTitle()
        binding!!.bottomNavMessage.text = getBottomNavigationText()
        bottomNavLink.text = getBottomNavigationLink()
        authButton.text = getButtonText()
        binding!!.forgotPassTextView.visibility = getForgotPasswordVisibility()

        authButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (TextUtils.isEmpty(email)) {
                emailInputLayout.error = "Error email. Email can't be empty"
                return@setOnClickListener
            } else {
                if (!isValidEmail(email)) {
                    emailInputLayout.error = "Error email. Enter the correct email"
                    return@setOnClickListener
                }
                emailInputLayout.error = null
            }

            if (TextUtils.isEmpty(password)) {
                passwordInputLayout.error = "Error password. Password can't be empty"
                return@setOnClickListener
            } else {
                if (password.length < 6) {
                    passwordInputLayout.error =
                        "Error password. Password should be at least 6 symbols"
                    return@setOnClickListener
                }
                passwordInputLayout.error = null
            }

            onCLickButtonEvent(email, password)
        }

        bottomNavLink.setOnClickListener { onClickLinkEvent() }
    }

    private fun isValidEmail(email: String): Boolean {
        val regexPattern = ("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
        return Pattern.compile(regexPattern).matcher(email).matches()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}