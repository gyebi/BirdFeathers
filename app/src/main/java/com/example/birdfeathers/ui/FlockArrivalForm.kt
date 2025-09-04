package com.example.birdfeathers.ui

import android.R.attr.text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.birdfeathers.data.FlockArrival
import com.example.birdfeathers.viewmodel.FlockArrivalViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlockArrivalForm(
    onSave: ((FlockArrival) -> Unit)? = null,
    navController: NavController,
    viewModel: FlockArrivalViewModel) {

    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // State variables
    var arrivalDate by remember {
        mutableStateOf(
            LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        )
    }
    var farmName by remember { mutableStateOf("") }
    var coopNumber by remember { mutableStateOf("") }
    var dayOldChicks by remember { mutableStateOf("") }

    //var showSavedMsg by remember { mutableStateOf(false) }

    val birdTypes = listOf("BROILER", "LAYER", "GUINEA")
    var selectedType by remember { mutableStateOf(birdTypes[0]) }
    var expanded by remember { mutableStateOf(false) }


    Scaffold(snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
    },
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
                        Icon(Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF10B981))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White // 👈 TopBar background
                )
            )
        }

    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(100.dp))

            Text(
                text = "Day Old Chick Flock Arrival",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = arrivalDate,
                onValueChange = { arrivalDate = it },
                label = { Text("Arrival Date (yyyy-MM-dd)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = farmName,
                onValueChange = { farmName = it },
                label = { Text("Farm Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = coopNumber,
                onValueChange = { coopNumber = it },
                label = { Text("Coop Number") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Dropdown for Bird Type
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedType,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Bird Type") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .clickable { expanded = true }
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                )
                {
                    birdTypes.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type) },
                            onClick = {
                                selectedType = type
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = dayOldChicks,
                onValueChange = { dayOldChicks = it },
                label = { Text("Number of Day Old Chicks") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (farmName.isNotBlank() && coopNumber.isNotBlank() && dayOldChicks.isNotBlank()) {
                        val entry = FlockArrival(
                            arrivalDate = arrivalDate,
                            farmName = farmName,
                            coopNumber = coopNumber,
                            birdType = selectedType,
                            dayOldChicks = dayOldChicks.toIntOrNull() ?: 0
                        )
                        viewModel.saveFlockArrival(entry)

                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Saved successfully!",
                                withDismissAction = true
                            )
                        }

                        //reset form fields
                        arrivalDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
                        farmName = ""
                        coopNumber = ""
                        selectedType = ""
                        dayOldChicks = ""
                        focusManager.clearFocus()
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("Please fill all fields correctly")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null)
                Spacer(Modifier.size(8.dp))
                Text("Save Entry")
            }
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { navController.navigate("display_flock_docs") })
            {
                Text("Check DOCs So Far")
            }
        }
    }
}



/*
@Composable
fun FlockArrivalScreen() {
    FlockArrivalForm { //arrival ->
        // Save to DB, show a message, etc.
        // For now, just print to log or show a snackbar
        println("New Flock: $arrival")
    }
}

 */