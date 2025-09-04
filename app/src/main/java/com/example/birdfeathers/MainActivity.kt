package com.example.birdfeathers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.birdfeathers.navigation.AppNavigation
import com.example.birdfeathers.ui.theme.BirdFeathersTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge()

        FirebaseApp.initializeApp(this)

        setContent {
            BirdFeathersTheme {
                AppNavigation()

                }
            }
        }
    }





