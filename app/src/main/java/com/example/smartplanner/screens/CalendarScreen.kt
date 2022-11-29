package com.example.smartplanner.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.smartplanner.model.Routes
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.day.DayState
import io.github.boguszpawlowski.composecalendar.selection.DynamicSelectionState
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(navController: NavController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("smarty", Context.MODE_PRIVATE)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Smart Planner", fontSize = 22.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome ${sharedPreferences.getString("name", "")}",
                fontSize = 30.sp,
                modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            SelectableCalendar(
                dayContent = { dayState ->
                    val date = dayState.date
                    val dateString = DateTimeFormatter.ofPattern("dd-MM-yy").format(date)
                    DayCard(dayState = dayState) {
                        navController.navigate(Routes.EventScreen+"/$dateString")
                    }
                },
                modifier = Modifier.padding(5.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayCard(dayState: DayState<DynamicSelectionState>, onClick: ()-> Unit) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("smarty", Context.MODE_PRIVATE)
    val date = dayState.date
    val dateString = DateTimeFormatter.ofPattern("dd-MM-yy").format(date)
    val hasEvent = !sharedPreferences.getString(dateString, "").isNullOrEmpty()

    ElevatedCard(
        modifier = Modifier
            .alpha(if (dayState.isFromCurrentMonth) 1.0f else 0.4f)
            .aspectRatio(1.0f)
            .padding(5.dp),
        onClick = onClick,
        enabled = dayState.isFromCurrentMonth && LocalDate.now().isBefore(date.plusDays(1)),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(color = if (!hasEvent) Color.Transparent else Color.Green)
        ) {
            Text(text = date.dayOfMonth.toString(), fontWeight = if(hasEvent) FontWeight.Bold else FontWeight.Normal)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarScreenPreview() {
    CalendarScreen(rememberNavController())
}