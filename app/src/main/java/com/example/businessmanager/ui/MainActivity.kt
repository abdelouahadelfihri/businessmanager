package com.example.businessmanager.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.example.businessmanager.navigation.BizApp
import com.example.businessmanager.ui.theme.BizTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BizTheme {
                // Compact = phone portrait, Medium = foldable / small tablet, Expanded = tablet / desktop
                val windowSize = calculateWindowSizeClass(this@MainActivity)
                BizApp(widthClass = windowSize.widthSizeClass)
            }
        }
    }
}
