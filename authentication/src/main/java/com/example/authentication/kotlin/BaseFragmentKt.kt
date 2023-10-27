package com.example.authentication.kotlin

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.example.authentication.R
import com.example.authentication.databinding.FragmentBaseBinding
import com.example.authentication.ui.textFieldTextStyle
import com.example.authentication.ui.theme.poppinsFontFamily
import com.example.authentication.ui.theme.webOrange
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
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.composeBaseFragmentBody.setContent {

            var email by rememberSaveable { mutableStateOf("") }
            var emailIsValid by rememberSaveable { mutableStateOf(true) }
            var emailErrorMessage by rememberSaveable { mutableStateOf("") }
            val emailInteractionSource = remember { MutableInteractionSource() }
            val isEmailFocused by emailInteractionSource.collectIsFocusedAsState()

            var emailBorderColor = if (isEmailFocused) {
                webOrange
            } else {
                Color.Transparent
            }
            if (!emailIsValid) {
                emailBorderColor = Color.Red
            }

            var password by rememberSaveable { mutableStateOf("") }
            var passwordIsValid by rememberSaveable { mutableStateOf(true) }
            var passwordErrorMessage by rememberSaveable { mutableStateOf("") }
            val passwordInteractionSource = remember { MutableInteractionSource() }
            val isPasswordFocused by passwordInteractionSource.collectIsFocusedAsState()
            val showPassword = rememberSaveable { mutableStateOf(false) }

            var passwordBorderColor = if (isPasswordFocused) {
                webOrange
            } else {
                Color.Transparent
            }
            if (!passwordIsValid) {
                passwordBorderColor = Color.Red
            }

            val textFieldColors = TextFieldDefaults.colors(
                focusedContainerColor = colorResource(id = R.color.light_gray),
                unfocusedContainerColor = colorResource(id = R.color.light_gray),
                unfocusedLabelColor = colorResource(id = R.color.bellara),
                focusedLabelColor = colorResource(id = R.color.web_orange),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )

            fun validateEmail(email: String): Boolean {
                if (TextUtils.isEmpty(email)) {
                    emailErrorMessage = "Error email. Email can't be empty"
                    emailBorderColor = Color.Red
                    return false
                } else {
                    if (!isValidEmail(email)) {
                        emailErrorMessage = "Error email. Enter the correct email"
                        emailBorderColor = Color.Red
                        return false
                    }
                    emailErrorMessage = ""
                    emailBorderColor = if (isEmailFocused) {
                        webOrange
                    } else {
                        Color.Transparent
                    }
                    return true
                }
            }

            fun validatePassword(password: String): Boolean {
                if (TextUtils.isEmpty(password)) {
                    passwordErrorMessage = "Error password. Password can't be empty"
                    passwordBorderColor = Color.Red
                    return false
                } else {
                    if (password.length < 6) {
                        passwordErrorMessage =
                            "Error password. Password should be at least 6 symbols"
                        passwordBorderColor = Color.Red
                        return false
                    }
                    passwordErrorMessage = ""
                    passwordBorderColor = if (isPasswordFocused) {
                        webOrange
                    } else {
                        Color.Transparent
                    }
                    return true
                }
            }

            Scaffold(
                modifier = Modifier
                    .fillMaxSize(),
                topBar = {
                    AppBar(
                        modifier = Modifier,
                        title = getTitle()
                    )
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .background(webOrange)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 11.dp)
                            .background(colorResource(id = R.color.white)),
                        shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp),
                    ) {
                        //Email text field
                        TextField(
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .padding(top = 62.dp)
                                .fillMaxWidth()
                                .height(65.dp)
                                .border(
                                    2.dp,
                                    emailBorderColor,
                                    RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                                ),
                            value = email,
                            onValueChange = { email = it },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            interactionSource = emailInteractionSource,
                            textStyle = textFieldTextStyle,
                            colors = textFieldColors,
                            label = {
                                TextFieldLabel(text = stringResource(id = R.string.user_name_hint))
                            },
                            isError = !emailIsValid,
                            shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
                        )

                        // Email error message
                        if (!emailIsValid) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 20.dp, top = 2.dp, bottom = 5.dp),
                                text = emailErrorMessage,
                                color = colorResource(id = R.color.red),
                                fontSize = 10.sp,
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.W500
                            )
                        } else {
                            Spacer(modifier = Modifier.height(22.dp))
                        }

                        // Password text field
                        TextField(
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .fillMaxWidth()
                                .height(65.dp)
                                .border(
                                    2.dp,
                                    passwordBorderColor,
                                    RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                                ),
                            value = password,
                            onValueChange = { password = it },
                            singleLine = true,
                            visualTransformation = if (showPassword.value)
                                VisualTransformation.None
                            else PasswordVisualTransformation('*'),
                            trailingIcon = {
                                PasswordTrailingIcon(
                                    showPassword = showPassword,
                                    passwordIsValid = passwordIsValid,
                                    passwordErrorMessage = passwordErrorMessage
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            interactionSource = passwordInteractionSource,
                            textStyle = textFieldTextStyle,
                            colors = textFieldColors,
                            label = {
                                TextFieldLabel(text = stringResource(id = R.string.password_hint))
                            },
                            isError = !passwordIsValid,
                            shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
                        )

                        //Password error message
                        if (!passwordIsValid) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 20.dp, top = 2.dp, bottom = 12.dp),
                                text = passwordErrorMessage,
                                color = colorResource(id = R.color.red),
                                fontSize = 10.sp,
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.W500
                            )
                        } else {
                            Spacer(modifier = Modifier.height(29.dp))
                        }

                        // Forgot password link
                        if (getForgotPasswordVisibility() == View.VISIBLE) {
                            Text(
                                modifier = Modifier
                                    .align(Alignment.End)
                                    .padding(end = 20.dp),
                                text = stringResource(id = R.string.forgot_pass),
                                style = TextStyle(
                                    color = colorResource(id = R.color.web_orange),
                                    fontSize = 14.sp,
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.W500,
                                )
                            )
                        }

                        //Auth button
                        Button(
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .padding(top = 59.dp, bottom = 81.dp)
                                .fillMaxWidth()
                                .height(62.dp),
                            onClick = {
                                emailIsValid = validateEmail(email)
                                passwordIsValid = validatePassword(password)

                                if (emailIsValid && passwordIsValid) {
                                    onCLickButtonEvent(email, password)
                                }
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                colorResource(id = R.color.gray)
                            )
                        ) {
                            Text(
                                text = getButtonText(),
                                style = TextStyle(
                                    color = colorResource(id = R.color.white),
                                    fontSize = 14.sp,
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.W500
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(44.dp))

                    Text(
                        text = getBottomNavigationText(),
                        style = TextStyle(
                            color = colorResource(id = R.color.white),
                            fontSize = 14.sp,
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.W500
                        )
                    )

                    Text(
                        modifier = Modifier
                            .clickable { onClickLinkEvent() },
                        text = getBottomNavigationLink(),
                        style = TextStyle(
                            color = colorResource(id = R.color.white),
                            fontSize = 14.sp,
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.W600
                        )
                    )
                }
            }
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