package com.kinhealth.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.kinhealth.app.ui.navigation.KinHealthMainApp
import com.kinhealth.app.ui.theme.KinHealthTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KinHealthTheme {
                KinHealthMainApp()
            }
        }
    }
}

