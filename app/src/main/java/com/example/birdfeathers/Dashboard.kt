package com.example.birdfeathers

import MinimalFlockAlertCard
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.birdfeathers.navigation.BottomNavBar
import com.example.birdfeathers.viewmodel.EggCollectionViewModel
import com.example.birdfeathers.viewmodel.FlockArrivalViewModel
import com.example.birdfeathers.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun Dashboard(
    navController: NavHostController,
    viewModel: UserViewModel,
    eggCollectionViewModel: EggCollectionViewModel,
    flockArrivalViewModel: FlockArrivalViewModel
) {

    val currentUser = viewModel.currentUser.collectAsState()
    val scrollState = rememberScrollState()

    val firebaseUser = FirebaseAuth.getInstance().currentUser


    Scaffold(
        topBar = {
            TopBarWithMenu(onMenuItemSelected = { item ->
                when (item) {
                    "Chicken Stats" -> navController.navigate("chicken_count_route")
                    "Financials" -> navController.navigate("financials_home")
                    "Feed Schedule" -> navController.navigate("feeds_home")
                    "Eggs Collection" -> navController.navigate("egg_collection_home")
                    "Total Eggs Collected" -> navController.navigate("daily_egg_collection_route")
                    "Eggs Chart" -> navController.navigate("egg_collection_chart")
                }
            })
        },
                bottomBar = { BottomNavBar(navController) }
                ) { innerPadding ->

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .background(Color(0xFFF5F5FF))
                        .padding(innerPadding) // 👈 important
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Spacer(modifier= Modifier.height(50.dp))
                    /*Text(
                        text = "Welcome, ${firebaseUser?.displayName ?: "User"}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Start,
                        fontSize = 20.sp
                    )
                    */
                    SummaryCard(viewModel = eggCollectionViewModel)

                    Spacer(modifier= Modifier.height(16.dp))

                    QuickActionCards(navController)

                    Spacer(modifier = Modifier.height(16.dp))

                    AnalyticsCard()

                    Spacer(modifier = Modifier.height(16.dp))

                    MinimalFlockAlertCard(viewModel = flockArrivalViewModel)

                }
            }
            }

                @Composable
                fun QuickActionCards(navController: NavHostController) {

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            DashboardCard(
                                title = "New Arrivals",
                                bgColor = Color(0xFFFF6B6B),
                                iconResId = R.drawable.chicks_2,
                                modifier = Modifier.weight(1f),
                                onClick = { navController.navigate("flock_arrival_form")}

                            )
                            DashboardCard(
                                title = "Financials",
                                bgColor = Color(0xFF4ECDC4),
                                iconResId = R.drawable.dollar_sign,
                                modifier = Modifier.weight(1f),
                                onClick = {navController.navigate("financials_home") }
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            DashboardCard(
                                title = "Feed Schedule",
                                bgColor = Color(0xFF5B67FF),
                                iconResId = R.drawable.sack_feed_grain,
                                modifier = Modifier.weight(1f),
                                onClick = { navController.navigate("feeds_home") }

                            )
                            DashboardCard(
                                title = "Eggs Collection",
                                bgColor = Color(0xFFFFC04E),
                                iconResId = R.drawable.eggs,
                                modifier = Modifier.weight(1f),
                                onClick = { navController.navigate("egg_collection_home") }
                            )
                        }
                    }
                }

                @Composable
                fun DashboardCard(
                    title: String,
                    bgColor: Color,
                    iconResId: Int,
                    modifier: Modifier = Modifier,
                    onClick: () -> Unit,
                ) {
                    Box(
                        modifier = modifier
                            .height(150.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(bgColor)
                            .clickable { onClick() }
                            .padding(12.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = title,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Image(
                            painter = painterResource(id = iconResId),
                            contentDescription = null,
                            modifier = Modifier
                                .size(36.dp)
                                .align(Alignment.TopEnd)

                        )
                    }
                }




                @Composable
                fun SummaryCard( viewModel: EggCollectionViewModel
                ) {

                    val todayLayingHens by viewModel.todayLayingHens.collectAsState(initial = 0) // hens counted
                    val todayTotal by viewModel.todayEggTotal.collectAsState(initial = 0)// eggs collected
                    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())


                    Card(
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier
                            .padding(16.dp)
                        ) {
                            Text("Laying Hens | Date : $today", fontWeight = FontWeight.Bold)
                            Text("Laying Flock: $todayLayingHens")
                            Text("Eggs Collected: $todayTotal")

                            //Text("ALERTS: 2 | Feed Level: OK")
                        }
                    }
                }


                @Composable
                fun AnalyticsCard() {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFE5E5F0) // light gray-violet
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Analytics",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black
                            )

                            Icon(
                                painter = painterResource(id = R.drawable.data_analytics_1),
                                contentDescription = "Analytics",
                                tint = Color.Gray,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }


                @OptIn(ExperimentalMaterial3Api::class)
                @Composable
                fun TopBarWithMenu(
                    onMenuItemSelected: (String) -> Unit
                ) {
                    var menuExpanded by remember { mutableStateOf(false) }
                    val dashboardItems = listOf(
                        "Chicken Stats", "Financials", "Feed Schedule",
                        "Eggs Collection", "Total Eggs Collected", "Eggs Chart"
                    )

                    TopAppBar(
                        title = {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "BirdFeathers",
                                    fontWeight = FontWeight.SemiBold,
                                    style = MaterialTheme.typography.titleLarge,
                                    textAlign = TextAlign.Center
                                )
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = { menuExpanded = true }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        },
                        actions = {
                            IconButton(onClick = { /* TODO: search action */ }) {
                                //Icon(Icons.Default.Search, contentDescription = "Search")
                            }

                            DropdownMenu(
                                expanded = menuExpanded,
                                onDismissRequest = { menuExpanded = false }
                            ) {
                                dashboardItems.forEach { item ->
                                    DropdownMenuItem(
                                        text = { Text(item) },
                                        onClick = {
                                            menuExpanded = false
                                            onMenuItemSelected(item)
                                        }
                                    )
                                }
                            }
                        }
                    )
                }








