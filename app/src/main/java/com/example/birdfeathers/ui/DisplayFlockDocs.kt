package com.example.birdfeathers.ui



    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.items
    import androidx.compose.material3.Card
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.collectAsState
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import androidx.navigation.NavHostController
    import com.example.birdfeathers.viewmodel.FlockArrivalViewModel


@Composable
    fun DisplayFlockDocs(
        navController: NavHostController,
        viewModel: FlockArrivalViewModel
    ) {
        val allFlocksState = viewModel.allFlockArrivals.collectAsState(initial = emptyList())
        val allFlocks = allFlocksState.value


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(100.dp))

            Text(
                "Day Old Chicks History",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(allFlocks) { flock ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text("Date: ${flock.arrivalDate} |Farm Name: ${flock.farmName} ")
                            Text("Flock Count: ${flock.dayOldChicks}  |  ${flock.birdType}")


                        }
                    }
                }
            }
        }
    }

