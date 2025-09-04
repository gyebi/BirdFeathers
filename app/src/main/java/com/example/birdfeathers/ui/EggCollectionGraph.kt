package com.example.birdfeathers.ui

import android.graphics.Color.green
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.birdfeathers.viewmodel.EggCollectionViewModel
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData
import com.github.tehras.charts.piechart.PieChartData.Slice
import com.github.tehras.charts.piechart.animation.simpleChartAnimation
import com.github.tehras.charts.piechart.renderer.SimpleSliceDrawer
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EggCollectionGraph(
    navController: NavHostController,
    viewModel: EggCollectionViewModel
) {
    val allEggs by viewModel.allEggs.collectAsState(initial = emptyList())

    // At the top of EggCollectionGraph, after you have allEggs and selectedPeriod

    var selectedPeriod by remember { mutableStateOf("Today") }
    val periods = listOf("Today", "Weekly", "Monthly")

    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    //val weekStart = today.minusDays(today.dayOfWeek.value.toLong() - 1)
    //val weekEnd = weekStart.plusDays(6)

    //Filter by selected period
    val filteredEggs = when (selectedPeriod) {
        "Today" -> {
            val todayStr = today.format(formatter)
            allEggs.filter { it.date == todayStr }
        }

        "Weekly" -> {
            val weekAgo = today.minusDays(6) // Last 7 days: today + 6 previous days
            allEggs.filter {
                val entryDate = runCatching { LocalDate.parse(it.date, formatter) }.getOrNull()
                entryDate != null && !entryDate.isBefore(weekAgo) && !entryDate.isAfter(today)
            }
        }

        "Monthly" -> {
            val thisMonth = today.monthValue
            val thisYear = today.year
            allEggs.filter {
                val entryDate = runCatching { LocalDate.parse(it.date, formatter) }.getOrNull()
                entryDate != null && entryDate.monthValue == thisMonth && entryDate.year == thisYear
            }
        }

        else -> allEggs
    }

    allEggs // TODO: month logic

    val eggCount = filteredEggs.sumOf { it.eggCount }
    val flockCount = filteredEggs.minOfOrNull { it.flockCount } ?: 0

    val percentLay = if (flockCount == 0) 0f else (eggCount.toFloat() / flockCount.toFloat()) * 100f
    val percentNotLay =
        if (flockCount == 0) 0f else ((flockCount - eggCount).toFloat() / flockCount.toFloat()) * 100f

    // To display with 1 decimal place (as String)
    //val percentLayStr = String.format("%.1f", percentLay)
    //val percentNotLayStr = String.format("%.1f", percentNotLay)

// If you want to keep as Float rounded to 1 decimal:
    //val percentLayRounded = "%.1f".format(percentLay).toFloat()
    //  val percentNotLayRounded = "%.1f".format(percentNotLay).toFloat()

    Scaffold(
        containerColor = Color.White, // 👈 Scaffold background
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Back",
                        color = Color.Black // 👈 Title text color
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF10B981)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White // 👈 TopBar background
                )
            )
        }
    ) { paddingValues ->

        Column(
            // Optional properties.
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Spacer(modifier = Modifier.height(100.dp))

            Text(
                text = "Lay Ratio ",
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp
            )

            Spacer(modifier = Modifier.height(50.dp))


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)

            ) {

                var expanded by remember { mutableStateOf(false) }
                Button(onClick = { expanded = true }) {
                    Text(selectedPeriod)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }) {
                    periods.forEach { period ->
                        DropdownMenuItem(
                            text = { Text(period) },
                            onClick = {
                                selectedPeriod = period
                                expanded = false
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(50.dp))

                EggLayPieChart(percentLay, percentNotLay)
                Spacer(modifier = Modifier.height(50.dp))


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {


                    Spacer(modifier = Modifier.weight(1f))

                    Spacer(modifier = Modifier.weight(1f))

                }
            }
        }
    }
}

@Composable
fun EggLayPieChart(
    percentLay: Float,
    percentNotLay: Float
) {
    val green = Color(0xFF10B981)
    val amber = Color(0xFFFBBF24)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Lay Ratio Chart",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Pie Chart
        PieChart(
            pieChartData = PieChartData(
                slices = listOf(
                    Slice(percentLay, green),
                    Slice(percentNotLay, amber)
                )
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(16.dp),
            animation = simpleChartAnimation(),
            sliceDrawer = SimpleSliceDrawer(100f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Legend
        Row(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            PieLegendItem(color = green, label = "Laid", percentage = percentLay)
            Spacer(modifier = Modifier.width(16.dp))
            PieLegendItem(color = amber, label = "Not Laid", percentage = percentNotLay)
        }
    }
}

@Composable
fun PieLegendItem(color: Color, label: String, percentage: Float) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .height(14.dp)
                .width(14.dp)
                .background(color)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            "$label (${String.format("%.1f", percentage)}%)",
            fontSize = 14.sp
        )
    }
}
