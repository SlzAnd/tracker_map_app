package com.example.map_app.kotlin.presentation

import android.view.ContextThemeWrapper
import android.widget.CalendarView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.authentication.ui.theme.poppinsFontFamily
import com.example.map_app.R
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    modifier: Modifier,
    exitButtonEvent: () -> Unit,
    calendarButtonEvent: () -> Unit,
) {
    TopAppBar(
        title = {
            Box(
                modifier = modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.timeline),
                    color = colorResource(id = R.color.white),
                    fontSize = 14.sp,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight(500)
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = { calendarButtonEvent() }) {
                Icon(
                    painter = painterResource(id = R.drawable.calendar),
                    contentDescription = "open calendar"
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
fun BottomClockIcon(
    modifier: Modifier,
    selectedDateText: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = modifier
                .padding(top = 10.dp)
                .weight(0.9f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.orange_circle),
                contentDescription = "selected date icon"
            )
            Image(
                painter = painterResource(id = R.drawable.time),
                contentDescription = "selected date icon"
            )
        }
        Text(
            modifier = Modifier
                .padding(bottom = 8.dp),
            text = selectedDateText,
            color = Color.White,
            fontSize = 7.sp,
            fontFamily = com.example.map_app.kotlin.ui.theme.poppinsFontFamily,
            fontWeight = FontWeight.W300
        )
    }
}

@Composable
fun CalendarDialog(
    modifier: Modifier,
    onDateSelected: (LocalDate) -> Unit,
    onOkButtonEvent: () -> Unit,
) {

    Card(
        modifier = modifier.wrapContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.white)
        ),
        shape = RoundedCornerShape(7.dp)
    ) {
        Column(
            modifier
                .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Datepicker",
                fontSize = 30.sp,
            )

            CustomCalendarView {
                onDateSelected(it)
            }

            Button(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 20.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                onClick = {
                    onOkButtonEvent()
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    colorResource(id = R.color.web_orange)
                )
            ) {
                Text(
                    text = "Ok",
                    style = TextStyle(
                        color = colorResource(id = R.color.white),
                        fontSize = 14.sp,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.W500
                    )
                )
            }
        }
    }
}

@Composable
fun CustomCalendarView(onDateSelected: (LocalDate) -> Unit) {
    AndroidView(
        modifier = Modifier.wrapContentSize(),
        factory = { context ->
            CalendarView(ContextThemeWrapper(context, R.style.CalenderViewCustom))
        },
        update = { view ->
            view.setOnDateChangeListener { _, year, month, dayOfMonth ->
                onDateSelected(
                    LocalDate
                        .now()
                        .withMonth(month + 1)
                        .withYear(year)
                        .withDayOfMonth(dayOfMonth)
                )
            }
        }
    )
}