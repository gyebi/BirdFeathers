package com.example.birdfeathers.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.example.birdfeathers.data.EggStatsEntry

@Composable
fun EggStatsLineChart(chartData: List<EggStatsEntry>) {
    if (chartData.isEmpty()) {
        Text("No data available")
        return
    }

    val maxEggs = chartData.maxOf { it.eggCount }.coerceAtLeast(chartData.maxOf { it.targetLay.toInt() })
    val minEggs = 0
    val spacing = 60f

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(
            text = "Egg Production Chart",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Canvas(modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
        ) {
            val spacePerItem = (size.width - spacing) / (chartData.size - 1)

            val heightRatio = size.height / (maxEggs - minEggs).toFloat()

            // Line path for actual eggs
            val eggPath = Path().apply {
                chartData.forEachIndexed { index, entry ->
                    val x = spacing + index * spacePerItem
                    val y = size.height - (entry.eggCount - minEggs) * heightRatio
                    if (index == 0) moveTo(x, y) else lineTo(x, y)
                }
            }

            // Draw actual egg collection line
            drawPath(eggPath, color = Color.Blue, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f))

            // Draw target lay line
            val targetY = size.height - (chartData.first().targetLay - minEggs) * heightRatio
            drawLine(
                color = Color.Red,
                start = Offset(spacing, targetY),
                end = Offset(size.width, targetY),
                strokeWidth = 2f
            )

            // Draw X-axis labels (dates)
            chartData.forEachIndexed { index, entry ->
                val x = spacing + index * spacePerItem
                drawContext.canvas.nativeCanvas.drawText(
                    entry.date.takeLast(5), // show MM-dd
                    x,
                    size.height + 20f,
                    android.graphics.Paint().apply {
                        textAlign = android.graphics.Paint.Align.CENTER
                        textSize = 30f
                        color = android.graphics.Color.BLACK
                    }
                )
            }
        }
    }
}
