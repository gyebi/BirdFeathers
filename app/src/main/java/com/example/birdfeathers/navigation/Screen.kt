package com.example.birdfeathers.navigation

sealed class Screen(val route: String) {
    object SplashScreen : Screen("splash")
    object LoginSignUp: Screen("login_signup")
    object LoginPassword:Screen("login_password")
    object SignUp : Screen("signup")
    object Dashboard : Screen("dashboard")
    object Flock : Screen("flock")
    object FinancialsHome : Screen("financials_home")
    object Financials : Screen("financials")
    object ChickenCount : Screen("chicken_count")
    object FeedSchedule : Screen("feed_schedule")
    object BroilerProjectionForm : Screen("broiler_projection_form")
    object DatabaseVerifyScreen : Screen("dbase_verify")
    object EggCollectionHome : Screen("egg_collection_home")
    object EggCollectionEntry : Screen("egg_collection_entry")
    object EggCollectionGraph : Screen("egg_collection_graph")
    object DisplayEggCollection : Screen("display_egg_collection")
    object FlockArrivalForm : Screen("flock_arrival_form")
    object DisplayFlockDocs : Screen("display_flock_docs")
    object FeedsHome : Screen("feeds_home")
    object FeedCalculationHome : Screen("feed_calculation_home")
    object FeedCalculatorKg : Screen("calc_feed_cost_kg")
    object FeedCalculatorBags : Screen("calc_feed_cost_bags")
    object EggAnalyticsScreen : Screen("egg_analytics_screen")
    object ProjectionsBroilers : Screen("cost_projections_broilers_screen")




// Add more as needed
}