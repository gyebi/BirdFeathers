package com.example.birdfeathers.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun BroilerProjectionForm(navController: NavHostController,
    onSubmit: (numChicks: Int,
               feedCost: Double,
               mortalityPercent: Double,
               marketPrice: Double) -> Unit
) {
    // State variables for user input
    var numChicks by remember { mutableStateOf("") }
    var feedCost by remember { mutableStateOf("") }
    var mortalityPercent by remember { mutableStateOf("") }
    var marketPrice by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var resultText by remember { mutableStateOf<String>("") }


    val chicks = numChicks.toIntOrNull()
    val feed = feedCost.toDoubleOrNull()
    val mortality = mortalityPercent.toDoubleOrNull()
    val price = marketPrice.toDoubleOrNull()



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF4FC3F7), Color(0xFF81C784), Color(0xFFFCE38A))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(28.dp),
            modifier = Modifier
                .padding(20.dp)
                .widthIn(max = 370.dp)
                .wrapContentHeight(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFF9C4)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Broiler Projection Form",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontSize = 24.sp,
                        color = Color(0xFF1976D2)
                    )
                )
                Spacer(Modifier.height(18.dp))
                OutlinedTextField(
                    value = numChicks,
                    onValueChange = { numChicks = it },
                    label = { Text("Number of Chicks") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = feedCost,
                    onValueChange = { feedCost = it },
                    label = { Text("Feed Cost per Chick (GHS)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = mortalityPercent,
                    onValueChange = { mortalityPercent = it },
                    label = { Text("Mortality (%)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = marketPrice,
                    onValueChange = { marketPrice = it },
                    label = { Text("Market Price per Bird (GHS)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(14.dp))

                errorMessage?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                Button(
                    onClick = {
                        // Simple validation

                        if (chicks == null || feed == null || mortality == null || price == null) {
                            errorMessage = "Fill all fields with valid values."
                        } else {
                            errorMessage = null
                            onSubmit(chicks, feed, mortality, price)

                            // --- YOUR CALCULATION LOGIC ---
                            val surviving = chicks - (chicks * mortality / 100.0)
                            val totalFeedCost = chicks * feed
                            val totalRevenue = surviving * price
                            val profit = totalRevenue - totalFeedCost

                            resultText = """
                                Surviving Birds: ${"%.2f".format(surviving)}
                                Total Feed Cost: GHS ${"%.2f".format(totalFeedCost)}
                                Total Revenue: GHS ${"%.2f".format(totalRevenue)}
                                Estimated Profit: GHS ${"%.2f".format(profit)}
                            """.trimIndent()
                        }
                    },


                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1976D2),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Calculate Projection", fontSize = 18.sp)
                }
                Spacer(Modifier.height(16.dp))

                // Show the result
                resultText?.let {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFD0F8CE)
                        )
                    ) {
                        Column(Modifier.padding(18.dp)) {
                            Text("Projection Summary:", fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(6.dp))
                            Text(it, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}
