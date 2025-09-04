package com.example.birdfeathers.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.birdfeathers.data.EggStatsEntry
import com.example.birdfeathers.viewmodel.EggStatsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EggAnalyticsScreen(
    navController: NavHostController,
    eggStatsViewModel: EggStatsViewModel
) {
    val chartData = eggStatsViewModel.chartData.collectAsState().value
    var selectedTab by remember { mutableStateOf(0) } // 0 = Weekly, 1 = Monthly

    // trigger weekly data update
    LaunchedEffect(Unit) {
        if (selectedTab == 0) {
            eggStatsViewModel.getWeeklyEggStats()
        } else {
            eggStatsViewModel.getMonthlyEggStats()
        }

    }

    Scaffold(
        containerColor = Color.White, // 👈 Scaffold background
        topBar = {
            TopAppBar(
                title = { Text("Back") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF10B981))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White // 👈 TopBar background
            ) )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Toggle Tabs
            val tabs = listOf("Weekly", "Monthly")
            TabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, label ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(label) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (chartData.isEmpty()) {
                Text("No data to display")
            } else {
                BarChartSection(chartData = chartData)
            }
        }
    }
}

@Composable
fun BarChartSection(chartData: List<EggStatsEntry>) {

    val maxEggCount = chartData.maxOfOrNull { it.eggCount } ?: 0
    val barColor = Color(0xFFFFC107)

    // Handle case when chartData is empty to avoid crash
    if (chartData.isEmpty()) {
        Text("No egg collection data available.")
        return
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {

        Text("Egg Collection Over Time", style = MaterialTheme.typography.titleMedium)

        Box(modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)) {

            Canvas(modifier = Modifier.fillMaxSize()) {
                val barWidth = size.width / (chartData.size * 2)
                val space = barWidth
                val maxBarHeight = size.height * 0.8f

                //val maxEggs = chartData.maxOf { it.eggCount }

                // Y-Axis labels (e.g., 0, 100, 200,...)
                val steps = 5
                val stepValue = maxEggCount / steps
                for (i in 0..steps) {
                    val y = size.height - (i * maxBarHeight / steps)
                    drawContext.canvas.nativeCanvas.drawText(
                        "${i * stepValue}",
                        0f,
                        y,
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.BLACK
                            textSize = 24f
                        }
                    )
                }

                chartData.forEachIndexed { index, entry ->
                    val barHeight = (entry.eggCount.toFloat() / maxEggCount) * maxBarHeight
                    val x = index * (barWidth + space) + space

                    drawRoundRect(
                        color = barColor,
                        topLeft = Offset(x, size.height - barHeight),
                        size = Size(barWidth, barHeight),
                        cornerRadius = CornerRadius(8f, 8f)
                    )

                    // X-Axis labels (dates)
                    drawContext.canvas.nativeCanvas.drawText(
                        entry.date.takeLast(5),  // e.g., "09" from "2025-08-29"
                        x,
                        size.height + 24f,
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.BLACK
                            textSize = 24f
                        }
                    )
                }
            }
        }
    }

}
