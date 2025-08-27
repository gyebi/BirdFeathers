package com.example.birdfeathers.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun BagSizeToggle(
    selected: Int, // 45 or 50
    onToggle: (Int) -> Unit
) {
    Row {
        // 45kg Button
        Box(
            modifier = Modifier
                .weight(1f)
                .height(40.dp)
                .clip(RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp))
                .background(if (selected == 45) Color(0xFF222222) else Color(0xFFD3D3D3))
                .clickable { onToggle(45) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                "45kg",
                color = if (selected == 45) Color(0xFF76FF03) else Color.Black,
                fontWeight = FontWeight.Bold
            )
        }

        // 50kg Button
        Box(
            modifier = Modifier
                .weight(1f)
                .height(40.dp)
                .clip(RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp))
                .background(if (selected == 50) Color(0xFF222222) else Color(0xFFD3D3D3))
                .clickable { onToggle(50) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                "50kg",
                color = if (selected == 50) Color(0xFF76FF03) else Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
