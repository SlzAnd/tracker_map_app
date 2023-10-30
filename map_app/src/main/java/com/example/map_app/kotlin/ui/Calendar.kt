package com.example.map_app.kotlin.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.map_app.R


@Composable
fun Calendar(
    prevMonthText: String,
    nextMonthText: String,
) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MonthSelectionRow(prevMonthText = prevMonthText, nextMonthText = nextMonthText)

        Spacer(modifier = Modifier.height(20.dp))

        DayOfWeekRow()

        Spacer(modifier = Modifier.height(20.dp))

        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .wrapContentHeight(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            columns = GridCells.Fixed(7)
        ) {
            val numbers = (1..35).toList()
            
            items(numbers.size) {
                Box(
                    modifier = Modifier
                        .width(35.dp)
                        .height(35.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = it.toString())
                }
            }
        }

    }
}

@Composable
private fun MonthSelectionRow(
    prevMonthText: String,
    nextMonthText: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = { /*TODO*/ },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = colorResource(id = R.color.web_orange)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "left arrow",
                    modifier = Modifier
                        .size(150.dp),
                )
            }
            Text(
                text = prevMonthText,
                fontSize = 20.sp,
                color = colorResource(id = R.color.web_orange)
            )
        }

        Text(
            text = "October",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = nextMonthText,
                fontSize = 20.sp,
                color = colorResource(id = R.color.web_orange)
            )
            IconButton(
                onClick = { /*TODO*/ },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = colorResource(id = R.color.web_orange)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "right arrow",
                    modifier = Modifier
                        .size(150.dp),
                )
            }
        }
    }
}

@Composable
fun DayOfWeekRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 35.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(26.dp)
    ) {
        Text(
            text = "Sun",
            color = colorResource(id = R.color.bellara)
        )
        Text(
            text = "Mon",
            color = colorResource(id = R.color.bellara)
        )
        Text(
            text = "Tue",
            color = colorResource(id = R.color.bellara)
        )
        Text(
            text = "Wed",
            color = colorResource(id = R.color.bellara)
        )
        Text(
            text = "Thu",
            color = colorResource(id = R.color.bellara)
        )
        Text(
            text = "Fri",
            color = colorResource(id = R.color.bellara)
        )
        Text(
            text = "Sat",
            color = colorResource(id = R.color.bellara)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun calendarPreview() {
    Calendar(
        prevMonthText = "Sept",
        nextMonthText = "Nov"
    )
}