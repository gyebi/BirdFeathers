package com.example.birdfeathers.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.birdfeathers.viewmodel.FeedRequirementsViewModel

@Composable
fun FeedCostCalculatorKg(
    navController: NavHostController,
    viewModel: FeedRequirementsViewModel,
    onBack: () -> Unit

) {

    val starter by viewModel.starter.collectAsState()
    val grower by viewModel.grower.collectAsState()
    val finisher by viewModel.finisher.collectAsState()
    val selectedBirdType by viewModel.selectedBirdType.collectAsState()

    val starterTotal by viewModel.starterTotalFeed.collectAsState()
    val growerTotal by viewModel.growerTotalFeed.collectAsState()
    val finisherTotal by viewModel.finisherTotalFeed.collectAsState()


    val birdTypes = listOf("Broiler", "Layer", "Guinea")

    val totalFeed by viewModel.totalFeed.collectAsState()

    val numBirds by viewModel.numBirds.collectAsState()
    var birdsInput by remember { mutableStateOf(numBirds.toString()) }

    //val selectedBirdType by viewModel.selectedBirdType.collectAsState()
    /*val numBirds by viewModel.numBirds.collectAsState()
    val feedPrices by viewModel.feedPrices.collectAsState()
    val feedBreakdown = viewModel.feedBreakdown()
    val totalFeedKg = viewModel.totalFeedKg
    val totalFeedCost = viewModel.totalFeedCost
*/
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
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.Start
            ) {
                // Bird type selector
                Text("Select Bird Type:", style = MaterialTheme.typography.titleMedium)
                Row(
                    modifier = Modifier
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement
                        .spacedBy(12.dp)
                ) {
                    birdTypes.forEach { type ->
                        ElevatedButton(
                            onClick = { viewModel.loadFeedRequirements(type) },
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = if (type == selectedBirdType) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                                contentColor = if (type == selectedBirdType) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Text(type)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "Number of Birds",
                    style = MaterialTheme.typography.bodyLarge
                )

                OutlinedTextField(
                    value = birdsInput,
                    onValueChange = {
                        birdsInput = it
                        it.toIntOrNull()?.let(viewModel::setNumBirds)
                    },
                    label = { Text("Flock Size") },
                    singleLine = true,
                    modifier = Modifier.width(120.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Stage Feed per Bird
                Text(
                    "Stage Feed per Bird(kg/bird ):",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    modifier = Modifier.padding(start = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                )

                {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    )
                    {
                        OutlinedTextField(
                            value = starter,
                            onValueChange = {}, // disable editing for now
                            label = { Text("Starter") },
                            enabled = false,
                            modifier = Modifier.width(100.dp)
                        )

                        Text(
                            "Starter Total: ${"%.2f".format(starterTotal)} kg",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFF2196F3),
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }
                }
                Column(
                    modifier = Modifier.padding(start = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                )

                {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    )
                    {
                        OutlinedTextField(
                            value = grower,
                            onValueChange = {},
                            label = { Text("Grower") },
                            enabled = false,
                            modifier = Modifier.width(100.dp)
                        )
                        Text(
                            "Grower Total: ${"%.2f".format(growerTotal)} kg",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFFFF9800),
                                    modifier = Modifier
                                    .padding(start = 8.dp)
                                .align(Alignment.CenterVertically)
                        )

                    }
                }
                Column(
                    modifier = Modifier.padding(start = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                )

                {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    )
                    {
                        OutlinedTextField(
                            value = finisher,
                            onValueChange = {},
                            label = { Text("Finisher") },
                            enabled = false,
                            modifier = Modifier.width(100.dp)
                        )
                        Text(
                            "Finisher Total: ${"%.2f".format(finisherTotal)} kg",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFF4CAF50),
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }
                }
                Column(
                    modifier = Modifier.padding(start = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(thickness = 1.dp,
                        color = MaterialTheme.colorScheme.primary)




                    Spacer(modifier = Modifier.height(8.dp))


                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Total Feed: ${"%.2f".format(totalFeed)} kg",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color(0xFFF44336),

                            )


                    //Text("Total Feed: ${"%.2f".format(totalFeed)} kg", ...)

                    }

                }

            }

        }
    }
             /*
                feedPrices.forEach { (phase, price) ->
                    var priceInput by remember { mutableStateOf(price.toString()) }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 6.dp)
                    ) {
                        Text("$phase:", modifier = Modifier.width(80.dp))
                        OutlinedTextField(
                            value = priceInput,
                            onValueChange = {
                                priceInput = it
                                it.toDoubleOrNull()?.let { newPrice ->
                                    viewModel.setFeedPrice(
                                        phase,
                                        newPrice
                                    )
                                }
                            },
                            singleLine = true,
                            modifier = Modifier.width(100.dp)
                        )
                    }
                }


                Spacer(modifier = Modifier.height(16.dp))

                // Feed breakdown display
                Text("Feed Breakdown:", style = MaterialTheme.typography.titleMedium)
                Column(
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    feedBreakdown.forEach { (phase, totalKg, totalCost) ->
                        Text(
                            "$phase: ${"%.2f".format(totalKg)} kg — \$${"%.2f".format(totalCost)}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Total Feed: ${"%.2f".format(totalFeedKg)} kg",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Total Feed Cost: \$${"%.2f".format(totalFeedCost)}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
    */


