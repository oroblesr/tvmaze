package com.example.tvmaze

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.tvmaze.ui.main.MainScreen
import com.example.tvmaze.ui.theme.TvMazeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TvMazeTheme {
                MainScreen()
            }
        }
    }
}

