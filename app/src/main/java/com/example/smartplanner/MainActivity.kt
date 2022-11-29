package com.example.smartplanner

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.smartplanner.model.Routes
import com.example.smartplanner.screens.CalendarScreen
import com.example.smartplanner.screens.EventScreen
import com.example.smartplanner.screens.LoginScreen
import com.example.smartplanner.ui.theme.SmartPlannerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartPlannerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SmartPlannerApp()
                }
            }
        }
        val notificationChannel = NotificationChannel("smarty", "Smart Planner", NotificationManager.IMPORTANCE_HIGH).apply {
            description = "Some smarty thing"
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }
}

@Composable
fun SmartPlannerApp() {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("smarty",Context.MODE_PRIVATE)

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = if(sharedPreferences.getString("name","").isNullOrEmpty()) Routes.LoginScreen else Routes.CalendarScreen
    ) {
        composable(Routes.LoginScreen) {
            LoginScreen(navController = navController)
        }
        composable(Routes.CalendarScreen) {
            CalendarScreen(navController)
        }
        composable(Routes.EventScreen+"/{date}", arguments = listOf(navArgument("date") {type = NavType.StringType})) {
            val date = it.arguments!!.getString("date")!!
            EventScreen(navController = navController, dateString = date)
        }
    }
}
