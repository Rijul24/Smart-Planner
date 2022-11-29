package com.example.smartplanner.screens

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.smartplanner.MainActivity
import com.example.smartplanner.Notification
import com.example.smartplanner.R
import com.example.smartplanner.model.Event
import com.google.gson.Gson
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventScreen(navController: NavController, dateString: String) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("smarty", Context.MODE_PRIVATE)
    val eventString = sharedPreferences.getString(dateString, "")
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(title = {
            Text(text = dateString, modifier = Modifier.fillMaxWidth(), fontWeight = FontWeight.Bold)
        },
            navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "")
                }
            }
        )
        Box(modifier = Modifier.weight(1f)) {
            if(eventString.isNullOrEmpty()) {
                EventForm(navController, dateString)
            }
            else {
                EventDetails(event = Gson().fromJson(eventString, Event::class.java), navController)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventScreenPreview() {
    EventScreen(rememberNavController(), "10-11-22")
}

@Composable
fun EventDetails(event: Event, navController: NavController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("smarty", Context.MODE_PRIVATE)

    var dialogOpen by remember {
        mutableStateOf(false)
    }

    if(dialogOpen) {
        AlertDialog(
            onDismissRequest = { dialogOpen = false },
            title = {
                Text(text = "Are you sure you want to delete this event?")
            },
            confirmButton = {
                Button(onClick = {
                    sharedPreferences.edit()
                        .remove(event.date)
                        .apply()
                    Toast.makeText(context, "Event deleted", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }) {
                    Text(text = "YES")
                }
            },
            dismissButton = {
                Button(onClick = { dialogOpen = false }) {
                    Text(text = "NO")
                }
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(text = event.title, fontSize = 30.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Reminder At")
            Text(text = event.time, fontSize = 30.sp, fontWeight = FontWeight.Bold)
        }
        Button(onClick = {
            dialogOpen = true
        }) {
            Text(text = "Delete Reminder", fontSize = 20.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventForm(navController: NavController, dateString: String) {

    var title by remember {
        mutableStateOf("")
    }

    var time by remember {
        mutableStateOf("00:00")
    }

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("smarty", Context.MODE_PRIVATE)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Add Event", fontSize = 40.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(10.dp))
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            textStyle = TextStyle(fontSize = 20.sp),
            label = { Text(text = "Title")},
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(0.9f)
        )
        TimePicker(value = time, onValueChange = { time = it })
        Button(
            onClick = {
                if(title.isEmpty()) {
                    return@Button
                }
                val event = Event(title, dateString, time)
                sharedPreferences.edit()
                    .putString(dateString, Gson().toJson(event))
                    .apply()
                Toast.makeText(context, "Event added", Toast.LENGTH_SHORT).show()

                val day = dateString.substring(0 until 2).toInt()
                val month = dateString.substring(3 until 5).toInt() - 1
                val year = dateString.substring(6).toInt() + 2000
                val hour = time.substring(0 until 2).toInt()
                val min = time.substring(3).toInt()

                val intent = Intent(context, Notification::class.java)
                intent.putExtra("title", event.title)
                intent.putExtra("date", dateString)
                val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val calendar = Calendar.getInstance()
                calendar.set(year,month,day, hour, min, 0)
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
                Log.d("lol first", "${calendar.timeInMillis} ${System.currentTimeMillis()}")
                Log.d("lol sss", "$year $month $day $hour $min")
                Log.d("lol ds", calendar.toString())
                navController.popBackStack()
            },
            modifier = Modifier.padding(10.dp)
        ) {
            Text(text = "ADD", fontSize = 30.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePicker(
    value: String,
    onValueChange: (String) -> Unit,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    pattern: String = "HH:mm",
    is24HourView: Boolean = true,
) {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    val time = if (value.isNotBlank()) LocalTime.parse(value, formatter) else LocalTime.now()
    val dialog = TimePickerDialog(
        LocalContext.current,
        { _, hour, minute -> onValueChange(LocalTime.of(hour, minute).toString()) },
        time.hour,
        time.minute,
        is24HourView,
    )

    OutlinedTextField(
        value = value,
        enabled = false,
        modifier = Modifier
            .padding(10.dp)
            .wrapContentWidth()
            .clickable { dialog.show() },
        label = {Text("Time")},
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        textStyle = TextStyle(fontSize = 30.sp, textAlign = TextAlign.Center),
        onValueChange = onValueChange
    )
}