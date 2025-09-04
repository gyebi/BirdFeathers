package com.example.birdfeathers.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EggCollectionHome(navController: NavHostController) {
    Scaffold(
        containerColor = Color.White, // 👈 Scaffold background
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Back ",
                        color = Color.Black // 👈 Title text color
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF10B981))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White // 👈 TopBar background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        )
        {
            Spacer(modifier = Modifier.height(100.dp))

            // Logo placeholder

            Text(
                "Egg Collection",
                fontWeight = FontWeight.Bold, fontSize = 26.sp
            )

            Spacer(Modifier.height(24.dp))
            Button(
                onClick = { navController.navigate("egg_collection_form") },
                Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) { Text("Daily Egg Collection Form") }

            Button(
                onClick = { navController.navigate("egg_collection_chart") },
                Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) { Text("Egg Lay Ratio Chart") }
            Button(
                onClick = { navController.navigate("egg_analytics_screen") },
                Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) { Text("Egg Laying Periodic Chart") }
        }
    }
}

