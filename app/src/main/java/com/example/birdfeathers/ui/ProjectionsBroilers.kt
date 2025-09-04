
package com.example.birdfeathers.ui


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.birdfeathers.viewmodel.FinancialViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectionsBroilers (navController: NavHostController,viewModel: FinancialViewModel) {
    //Log.d("SCREEN", "ProjectionsBroilers Composable called")
    //ScreenLoader(isLoading = viewModel.isLoading) {
        //Log.d("SCREEN", "Inside ScreenLoader content")

        // Inside the ScreenLoader content
        //Column(Modifier.verticalScroll(rememberScrollState()).padding(16.dp)) {


        // Calculations

        val survivingBirds = (viewModel.numBirds * (1 - viewModel.mortalityRate / 100)).roundToInt()

        val totalCost = viewModel.dayOldChicksCost + viewModel.feedCost + viewModel.waterCost +
                viewModel.coopCost + viewModel.drinkersCost + viewModel.feedersCost +
                viewModel.medsCost + viewModel.shavingCost + viewModel.labourCost +
                viewModel.miscCost + viewModel.transportCost

    val expectedSales =
        ((viewModel.numBirds * (1 - viewModel.mortalityRate / 100)) * viewModel.salePricePerBird).roundToInt()

        val costPerBird = if (survivingBirds > 0) totalCost / survivingBirds else 0f

        val netProfit = expectedSales - totalCost

        val profitPerBird = if (survivingBirds > 0) netProfit / survivingBirds else 0f

        val roi = if (totalCost > 0) (netProfit / totalCost) * 100 else 0f



    Scaffold(
            containerColor = Color.White, // 👈 Scaffold background

            topBar = {
                TopAppBar(
                    title = { Text("Back") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White // 👈 TopBar background
                    )

                )
            }

        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(top = 120.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)

            ) {
                // Logo placeholder

                Text(
                    "Broiler Projections",
                    fontWeight = FontWeight.Bold, fontSize = 26.sp
                )

                Spacer(Modifier.height(12.dp))

                Text("Batch Setup", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                NumberInputField("Number of Birds", viewModel.numBirds) { viewModel.numBirds = it }
                FloatInputField("Mortality Rate (%)", viewModel.mortalityRate) {
                    viewModel.mortalityRate = it
                }
                FloatInputField("Expected Sale Price per Bird (GHS)", viewModel.salePricePerBird) {
                    viewModel.salePricePerBird = it
                }


                Spacer(Modifier.height(16.dp))


                Text("Cost Inputs", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                CostInputRow(
                    label = "Day Old Chicks",
                    viewModel.dayOldChicksCost
                ) { viewModel.dayOldChicksCost = it }
                CostInputRow(label = "Feed", viewModel.feedCost) { viewModel.feedCost = it }
                CostInputRow(label = "Water", viewModel.waterCost) { viewModel.waterCost = it }
                CostInputRow(label = "Coop", viewModel.coopCost) { viewModel.coopCost = it }
                CostInputRow(
                    label = "Drinkers",
                    viewModel.drinkersCost
                ) { viewModel.drinkersCost = it }
                CostInputRow(label = "Feeders", viewModel.feedersCost) {
                    viewModel.feedersCost = it
                }
                CostInputRow(
                    label = "Medicines & Vaccines",
                    viewModel.medsCost
                ) { viewModel.medsCost = it }
                CostInputRow(
                    label = "Wood Shavings",
                    viewModel.shavingCost
                ) { viewModel.shavingCost = it }
                CostInputRow(label = "Labour", viewModel.labourCost) {
                    viewModel.labourCost = it
                }
                CostInputRow(label = "Miscellaneous", viewModel.miscCost) {
                    viewModel.miscCost = it
                }
                CostInputRow(
                    label = "Transport",
                    viewModel.transportCost
                ) { viewModel.transportCost = it }


                Spacer(Modifier.height(12.dp))

                Text("Surviving Birds: $survivingBirds", fontWeight = FontWeight.SemiBold)
                Text("Cost Per Bird: GHS ${"%.2f".format(costPerBird)}",fontWeight = FontWeight.SemiBold)
                Text("Profit Per Bird: GHS ${"%.2f".format(profitPerBird)}",fontWeight = FontWeight.SemiBold)
                Text("ROI: ${"%.2f".format(roi)}%", fontWeight = FontWeight.SemiBold)
                Text(
                    "Total Cost: GHS ${"%.2f".format(totalCost)}",
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "Projected Sales (GHS ${viewModel.salePricePerBird} per bird): GHS $expectedSales",
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "Projected Net Profit: GHS ${"%.2f".format(netProfit)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(50.dp))
            }
        }
    }





