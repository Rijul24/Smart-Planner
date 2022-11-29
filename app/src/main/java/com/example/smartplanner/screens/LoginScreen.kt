package com.example.smartplanner.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.smartplanner.model.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("smarty", Context.MODE_PRIVATE)
    val navControllerBackStackEntry by navController.currentBackStackEntryAsState()

    var name by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Welcome to", fontSize = 40.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Text(text = "Smart Planner", fontSize = 50.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        OutlinedTextField(
            value = name, 
            onValueChange = { name = it }, 
            textStyle = TextStyle(fontSize = 18.sp),
            label = { Text(text = "Enter Name")},
            modifier = Modifier.padding(10.dp)
        )
        Button(onClick = {
            if(name.isEmpty()) {
                Toast.makeText(context, "Name can't be empty", Toast.LENGTH_SHORT).show()
                return@Button
            }
            sharedPreferences.edit()
                .putString("name", name)
                .apply()
            navController.navigate(Routes.CalendarScreen) {
                popUpTo(navControllerBackStackEntry!!.destination.route!!) {
                    inclusive = true
                }
            }
        }) {
            Text(text = "Submit", fontSize = 18.sp)
        }
    }

}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = rememberNavController())
}