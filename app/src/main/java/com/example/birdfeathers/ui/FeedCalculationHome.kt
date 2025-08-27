package com.example.birdfeathers.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun FeedCalculationHome(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF4FC3F7), Color(0xFF81C784), Color(0xFFFCE38A))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .padding(20.dp)
                .widthIn(max = 340.dp)
                .wrapContentHeight(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFF9C4)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "How would you like to calculate feed?",
                    style = MaterialTheme.typography.titleMedium
                )
                ElevatedButton(
                    onClick = { navController.navigate("calc_feed_cost_kg") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Calculate in Kilograms")
                }
                ElevatedButton(
                    onClick = { navController.navigate("calc_feed_cost_bags") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Calculate in Bags (50kg/45kg)")
                }
            }
        }
    }
}
