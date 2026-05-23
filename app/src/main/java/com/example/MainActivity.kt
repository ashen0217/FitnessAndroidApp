package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.ui.PulseFitApp
import com.example.ui.WorkoutViewModel
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Initialize the Workout State Engine ViewModel
        val viewModel = ViewModelProvider(this)[WorkoutViewModel::class.java]
        
        setContent {
            MyApplicationTheme {
                PulseFitApp(viewModel)
            }
        }
    }
}
