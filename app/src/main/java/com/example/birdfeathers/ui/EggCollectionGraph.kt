package com.example.birdfeathers.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
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
    val flockCount = filteredEggs.maxOfOrNull { it.flockCount } ?: 0

    val percentLay = if (flockCount == 0) 0f else (eggCount.toFloat() / flockCount.toFloat()) * 100f
    val percentNotLay = if (flockCount == 0) 0f else ((flockCount - eggCount).toFloat() / flockCount.toFloat()) * 100f

    // To display with 1 decimal place (as String)
    val percentLayStr = String.format("%.1f", percentLay)
    val percentNotLayStr = String.format("%.1f", percentNotLay)

// If you want to keep as Float rounded to 1 decimal:
    //val percentLayRounded = "%.1f".format(percentLay).toFloat()
  //  val percentNotLayRounded = "%.1f".format(percentNotLay).toFloat()



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

        Text(
            "Select a period:",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )


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

            PieChart(
                pieChartData = PieChartData(
                    slices = listOf(
                        Slice(percentLay, Color.Red),
                        Slice(percentNotLay, Color.Yellow)
                    )
                ),
                modifier = Modifier.fillMaxWidth()
                    .height(400.dp)
                    .padding(16.dp),
                animation = simpleChartAnimation(),
                sliceDrawer = SimpleSliceDrawer(100f)
            )
        }

        Spacer(modifier = Modifier.height(50.dp))

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {

            Text(
                "Laid: $eggCount",
                fontWeight = FontWeight.Bold,
                color = Color.Red,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                "Total Flock: ${flockCount}",
                fontWeight = FontWeight.Bold,
                color = Color.Green,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Text (
                 "Lay Ratio: %.1f".format(percentLay),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )

        }
        }
    }











