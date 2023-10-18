package com.example.authentication.kotlin

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.authentication.databinding.FragmentBaseBinding
import java.util.regex.Pattern

abstract class BaseFragmentKt : Fragment() {

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
        Log.d("TAG", "" + binding!!.root)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.toolbarTitle.text = getTitle()
        binding!!.bottomNavMessage.text = getBottomNavigationText()
        binding!!.bottomNavLink.text = getBottomNavigationLink()
        binding!!.authButton.text = getButtonText()
        binding!!.forgotPassTextView.visibility = getForgotPasswordVisibility()

        binding!!.authButton.setOnClickListener {
            val email = binding!!.userNameEditText.text.toString()
            val password = binding!!.passwordEditText.text.toString()

            if (!validateEmail(email)) {
                return@setOnClickListener
            }

            if (!validatePassword(password)) {
                return@setOnClickListener
            }

            onCLickButtonEvent(email, password)
        }

        binding!!.bottomNavLink.setOnClickListener { onClickLinkEvent() }
    }

    private fun validateEmail(email: String): Boolean {
        if (TextUtils.isEmpty(email)) {
            binding!!.userNameInputLayout.error = "Error email. Email can't be empty"
            return false
        } else {
            if (!isValidEmail(email)) {
                binding!!.userNameInputLayout.error = "Error email. Enter the correct email"
                return false
            }
            binding!!.userNameInputLayout.error = null
            return true
        }
    }

    private fun validatePassword(password: String): Boolean {
        if (TextUtils.isEmpty(password)) {
            binding!!.passwordInputLayout.error = "Error password. Password can't be empty"
            return false
        } else {
            if (password.length < 6) {
                binding!!.passwordInputLayout.error =
                    "Error password. Password should be at least 6 symbols"
                return false
            }
            binding!!.passwordInputLayout.error = null
            return true
        }
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