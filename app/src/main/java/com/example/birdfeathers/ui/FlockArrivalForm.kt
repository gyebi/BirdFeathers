package com.example.birdfeathers.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.birdfeathers.data.FlockArrival
import com.example.birdfeathers.viewmodel.FlockArrivalViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun FlockArrivalForm(
    onSave: ((FlockArrival) -> Unit)? = null,
    navController: NavController,
    viewModel: FlockArrivalViewModel){


    // State variables
    var arrivalDate by remember { mutableStateOf(LocalDate.now().format(DateTimeFormatter.ISO_DATE)) }
    var farmName by remember { mutableStateOf("") }
    var coopNumber by remember { mutableStateOf("") }
    var dayOldChicks by remember { mutableStateOf("") }
    var showSavedMsg by remember { mutableStateOf(false) }

    val birdTypes = listOf("BROILER", "LAYER", "GUINEA")
    var selectedType by remember { mutableStateOf(birdTypes[0]) }
    var expanded by remember { mutableStateOf(false) }



            Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(100.dp))

        // Logo placeholder
        Box(
            modifier = Modifier
                .size(60.dp)
            //.background(Color.Black, shape = CircleShape)

        )
        Text(
            text = "Day Old Chick Flock Arrival",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = arrivalDate,
            onValueChange = { arrivalDate = it },
            label = { Text("Arrival Date (yyyy-MM-dd)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = farmName,
            onValueChange = { farmName = it },
            label = { Text("Farm Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = coopNumber,
            onValueChange = { coopNumber = it },
            label = { Text("Coop Number") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Dropdown for Bird Type
        Box(modifier = Modifier.fillMaxWidth()){

            OutlinedTextField(
                value = selectedType,
                onValueChange = {},
                readOnly = true,
                label = { Text("Bird Type") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
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
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = dayOldChicks,
            onValueChange = { dayOldChicks = it },
            label = { Text("Number of Day Old Chicks") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (showSavedMsg){
            Text("Saved Successfully", color = MaterialTheme.colorScheme.primary)
        }
        Button(
            onClick = {
                if (farmName.isNotBlank() && coopNumber.isNotBlank() && dayOldChicks.isNotBlank()) {
                    val entry = FlockArrival(
                            arrivalDate=arrivalDate,
                            farmName=farmName,
                            coopNumber = coopNumber,
                            birdType = selectedType,
                            dayOldChicks = dayOldChicks.toIntOrNull() ?: 0
                        )
                    viewModel.saveFlockArrival(entry)
                    onSave?.invoke(entry)
                    //reset form fields
                    arrivalDate=LocalDate.now().format(DateTimeFormatter.ISO_DATE)
                    farmName=""
                    coopNumber = ""
                    selectedType = ""
                    dayOldChicks = ""

                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Entry")

        }
        Spacer(modifier = Modifier.height(50.dp))

        Button(
            onClick = { navController.navigate("display_flock_docs")})
        {
            Text("Check DOCs So Far")
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