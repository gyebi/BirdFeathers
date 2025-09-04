package com.example.birdfeathers.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.birdfeathers.entity.EggCollection
import com.example.birdfeathers.viewmodel.EggCollectionViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EggCollectionEntry(
    navController: NavController,
    onSave: ((EggCollection) -> Unit)? = null,
    viewModel: EggCollectionViewModel
){
    val today = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) }
    var date by remember { mutableStateOf(today) }
    var timeOfDay by remember { mutableStateOf("Morning") }
    var eggCount by remember { mutableStateOf("") }
    var flockCount by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var showTimeMenu by remember { mutableStateOf(false) }
    //var showDatePicker by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text("Back") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back",
                            tint = Color(0xFF10B981))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
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
            Box(
                modifier = Modifier
                    .size(60.dp)
                    //.background(Color.Black, shape = CircleShape)

            )
            Text(
                text = "Daily Egg Collection Entry",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )

            // Date (readonly for now, could add DatePicker later)
            OutlinedTextField(
                value = date,
                onValueChange = { date = it },
                label = { Text("Date") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                enabled = false // set to true if you want manual editing
            )

            // Time of day (dropdown)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                // Time of day (dropdown)
                OutlinedTextField(
                    value = timeOfDay,
                    onValueChange = {},
                    label = { Text("Time of Collection") },
                    placeholder = { Text("Select Time..") },
                    readOnly = true,
                    trailingIcon = {
                        Icon(Icons.Filled.ArrowDropDown,
                            contentDescription = "Show dropdown",
                            modifier = Modifier.clickable { showTimeMenu = true })
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showTimeMenu = true },
                )
                DropdownMenu(
                    expanded = showTimeMenu,
                    onDismissRequest = { showTimeMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Morning") },
                        onClick = {
                            timeOfDay = "Morning"
                            showTimeMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Evening") },
                        onClick = {
                            timeOfDay = "Evening"
                            showTimeMenu = false
                        }
                    )
                }
            }

            OutlinedTextField(
                value = eggCount,
                onValueChange = { eggCount = it },
                label = { Text("Number of Eggs Collected") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = flockCount,
                onValueChange = { flockCount = it },
                label = {
                    Text("Number of Birds (Flock Count)")
                        },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes / Observations") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                singleLine = false,
                maxLines = 3
            )

            Button(
                onClick = {
                    if (date.isNotBlank() && timeOfDay.isNotBlank() && eggCount.isNotBlank() && flockCount.isNotBlank()) {
                        val entry = EggCollection(
                            date = date,
                            timeOfDay = timeOfDay,
                            eggCount = eggCount.toIntOrNull() ?: 0,
                            flockCount = flockCount.toIntOrNull() ?: 0,
                            notes = notes
                        )
                        viewModel.saveEggCollection(entry)
                        onSave?.invoke(entry)                        // Reset fields clear fields
                        date = today
                        timeOfDay = ""
                        eggCount = ""
                        flockCount = ""
                        notes = ""
                        // Optionally show a snackbar or message (with Compose's SnackbarHost, etc)
                    }
                          },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Text("Save Entry")
            }

            Spacer(modifier = Modifier.height(50.dp))

            Button(
                onClick = { navController.navigate("display_egg_collection")})
            {
                Text("Check Eggs Collected")
            }
            }
    }
 }





