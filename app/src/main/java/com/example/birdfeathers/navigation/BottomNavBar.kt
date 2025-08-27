package com.example.birdfeathers.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Egg
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavBar(navController: NavHostController){

    val items = listOf(
        BottomNavItem("Dashboard", Icons.Default.Home, Screen.Dashboard.route),
        BottomNavItem("Egg Lay", Icons.Default.Egg, Screen.EggCollectionHome.route),
        BottomNavItem("Financials", Icons.Default.AttachMoney, Screen.FinancialsHome.route),
        BottomNavItem("Feed", Icons.Default.Agriculture, Screen.FeedsHome.route)
    )

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    //val navBackStackEntry by navController.currentBackStackEntryAsState()
    //val currentRoute = navBackStackEntry?.destination?.route


  /*  NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

*/
    NavigationBar (
        containerColor = Color(0xFF4CAF50)
    ) {
        items.forEach { item ->
            NavigationBarItem(
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                icon = { Icon(item.icon, contentDescription = item.label) },
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(Screen.Dashboard.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}
