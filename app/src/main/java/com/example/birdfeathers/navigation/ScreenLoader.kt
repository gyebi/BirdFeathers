package com.example.birdfeathers.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun ScreenLoader(
    isLoading: Boolean,
    timeoutMillis: Long = 500,
    content: @Composable () -> Unit
) {
    var showLoader by remember { mutableStateOf(false) }

    LaunchedEffect(isLoading) {
        if (isLoading) {
            delay(timeoutMillis)
            showLoader = true
        } else {
            showLoader = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (showLoader) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                strokeWidth = 4.dp,
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            content()
        }
    }
}
