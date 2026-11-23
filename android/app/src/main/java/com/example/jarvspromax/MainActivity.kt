package com.example.jarvspromax

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.jarvspromax.ui.home.HomeScreen
import com.example.jarvspromax.ui.theme.JarvsProMaxTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JarvsProMaxTheme {
                HomeScreen()
            }
        }
    }
}
