package com.example.birdfeathers.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import java.io.File
import java.io.FileOutputStream

@Composable
fun FeedsHome(
    navController: NavHostController

)


{
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                Text(
                    text = "Feeds",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Button(
                    onClick = {navController.navigate("feed_calculation_home") },
                    modifier = Modifier.fillMaxWidth()
                ){
                    Text("Feed Calculators")
                }
                Button(
                    onClick ={ navController.navigate("feed_stock") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Feed Stock")
                }
                Button(
                    onClick = { openPdf(context, "PoultryFeedReference.pdf") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("View Feed Reference PDF")
                }
            }
        }
    }
}



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
