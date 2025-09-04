package com.example.birdfeathers.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinancialsHome(navController: NavHostController) {

    val context = LocalContext.current
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
    )
    { paddingValues ->
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
                "Financials - Money Aspects of Poultry",
                fontWeight = FontWeight.Bold, fontSize = 26.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Log.d("NAVIGATION", "Navigating to cost_projections_broilers_screen")

            Button(onClick = { navController.navigate("cost_projections_broilers_screen") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Broiler Projections")
                    }
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                        onClick = { navController.navigate("feed_stock") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Layer Projections")
                    }
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                        onClick = { "" },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("View Excel Projections Sheet")
                    }
                }

            }
        }




/*
    fun openPdf(context: Context, assetFileName: String) {
        try {
            // Copy from assets to cache so we have a file URI
            val inputStream = context.assets.open(assetFileName)
            val outFile = File(context.cacheDir, assetFileName)
            val outputStream = FileOutputStream(outFile)
            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()

            // Create content URI with FileProvider
            val uri: Uri = FileProvider.getUriForFile(
                context,
                context.packageName + ".fileprovider",
                outFile
            )

            // Launch PDF viewer
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            // Show error toast/snackbar if you want
        }
    }
*/