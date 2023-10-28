package com.example.tracker_task.kotlin.presentation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.authentication.ui.theme.poppinsFontFamily
import com.example.tracker_task.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    modifier: Modifier,
    exitButtonEvent: () -> Unit
) {
    TopAppBar(
        title = {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.tracker),
                    color = colorResource(id = R.color.white),
                    fontSize = 14.sp,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight(500)
                )
            }
        },
        actions = {
            IconButton(onClick = { exitButtonEvent() }) {
                Icon(
                    painter = painterResource(id = R.drawable.exit),
                    contentDescription = "log out"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(id = R.color.web_orange)
        )
    )
}

@Composable
fun TrackerOffIcon() {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.radial_progress_1),
                    contentDescription = "tracker off"
                )
                Image(
                    modifier = Modifier
                        .padding(start = 3.dp),
                    painter = painterResource(id = R.drawable.location_1),
                    contentDescription = "tracker off"
                )
            }

            Text(
                modifier = Modifier
                    .padding(top = 32.dp),
                text = stringResource(id = R.string.tracker_off),
                fontFamily = poppinsFontFamily,
                fontSize = 17.sp,
                fontWeight = FontWeight.W500
            )
        }
    }
}

@Composable
fun TrackerOnIcon() {
    val infiniteTransition = rememberInfiniteTransition("rotation")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0F,
        targetValue = 360F,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing)
        ), label = ""
    )

    Box(
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier
                        .graphicsLayer { rotationZ = angle },
                    painter = painterResource(id = R.drawable.radial_progress_2),
                    contentDescription = "tracker on"
                )
                Image(
                    modifier = Modifier
                        .padding(start = 3.dp),
                    painter = painterResource(id = R.drawable.location_2),
                    contentDescription = "tracker on"
                )
            }

            Text(
                modifier = Modifier
                    .padding(top = 32.dp),
                text = stringResource(id = R.string.tracker_collect),
                fontFamily = poppinsFontFamily,
                fontSize = 17.sp,
                fontWeight = FontWeight.W500,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun GpsOffIcon() {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.radial_progress_3),
                    contentDescription = "gps off"
                )
                Image(
                    modifier = Modifier
                        .padding(start = 3.dp),
                    painter = painterResource(id = R.drawable.location),
                    contentDescription = "gps off"
                )
                Image(
                    modifier = Modifier
                        .padding(start = 47.dp, bottom = 45.dp),
                    painter = painterResource(id = R.drawable.exclamation_mark),
                    contentDescription = "gps off"
                )
            }

            Text(
                modifier = Modifier
                    .padding(top = 32.dp),
                text = stringResource(id = R.string.gps_off),
                fontFamily = poppinsFontFamily,
                fontSize = 17.sp,
                fontWeight = FontWeight.W500,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier
                    .padding(top = 7.dp),
                text = stringResource(id = R.string.cant_collect),
                fontFamily = poppinsFontFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.W300,
                textAlign = TextAlign.Center
            )
        }
    }
}