package com.example.birdfeathers.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun EggCollectionHome(navController: NavHostController)

{
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
            ) { Text("View Egg Collection Chart") }
        }
    }

