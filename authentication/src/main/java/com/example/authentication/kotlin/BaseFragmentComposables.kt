package com.example.authentication.kotlin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.authentication.R
import com.example.authentication.ui.theme.poppinsFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    modifier: Modifier,
    title: String
) {
    TopAppBar(
        title = {
            Box(
                modifier = modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    color = colorResource(id = R.color.white),
                    fontSize = 14.sp,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight(500)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(id = R.color.web_orange)
        )
    )
}

@Composable
fun TextFieldLabel(
    text: String
) {
    Text(
        modifier = Modifier
            .padding(start = 10.dp),
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight(500),
        fontFamily = poppinsFontFamily,
    )
}

@Composable
fun PasswordTrailingIcon(
    showPassword: MutableState<Boolean>,
    passwordIsValid: Boolean,
    passwordErrorMessage: String
) {
    var (icon, iconColor) = if (showPassword.value) {
        Pair(
            painterResource(R.drawable.baseline_visibility_24),
            colorResource(id = R.color.dark_gray)
        )
    } else {
        Pair(
            painterResource(R.drawable.baseline_visibility_off_24),
            colorResource(id = R.color.dark_gray)
        )
    }

    if (!passwordIsValid && passwordErrorMessage.isNotEmpty()) {
        iconColor = colorResource(id = R.color.red)
    }

    IconButton(onClick = { showPassword.value = !showPassword.value }) {
        Icon(
            icon,
            contentDescription = "Visibility",
            tint = iconColor
        )
    }
}
