package com.example.birdfeathers.navigation

import FeedRequirementsRepository
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.birdfeathers.BirdFeatherDatabase
import com.example.birdfeathers.Dashboard
import com.example.birdfeathers.domain.EggRepository
import com.example.birdfeathers.domain.FlockArrivalRepository
import com.example.birdfeathers.domain.UserRepository
import com.example.birdfeathers.ui.BroilerProjectionForm
import com.example.birdfeathers.ui.DisplayEggCollection
import com.example.birdfeathers.ui.DisplayFlockDocs
import com.example.birdfeathers.ui.EggCollectionEntry
import com.example.birdfeathers.ui.EggCollectionGraph
import com.example.birdfeathers.ui.EggCollectionHome
import com.example.birdfeathers.ui.FeedCalculationHome
import com.example.birdfeathers.ui.FeedCostCalculatorBags
import com.example.birdfeathers.ui.FeedCostCalculatorKg
import com.example.birdfeathers.ui.FeedsHome
import com.example.birdfeathers.ui.FinancialsHome
import com.example.birdfeathers.ui.FlockArrivalForm
import com.example.birdfeathers.ui.LoginSignUp
import com.example.birdfeathers.ui.SignUp
import com.example.birdfeathers.ui.SplashScreen
import com.example.birdfeathers.viewmodel.EggCollectionViewModel
import com.example.birdfeathers.viewmodel.EggCollectionViewModelFactory
import com.example.birdfeathers.viewmodel.FeedRequirementsViewModel
import com.example.birdfeathers.viewmodel.FeedRequirementsViewModelFactory
import com.example.birdfeathers.viewmodel.FlockArrivalViewModel
import com.example.birdfeathers.viewmodel.FlockArrivalViewModelFactory
import com.example.birdfeathers.viewmodel.SignUpViewModel
import com.example.birdfeathers.viewmodel.SignUpViewModelFactory
import com.example.birdfeathers.viewmodel.UserViewModel
import com.example.birdfeathers.viewmodel.UserViewModelFactory
import com.google.firebase.auth.FirebaseAuth


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    //Room Database + Firebase elements
    val context = LocalContext.current.applicationContext
    val database = remember { BirdFeatherDatabase.Companion.getDatabase(context) }



    val repository = remember {
        UserRepository(
            userDao = database.userDao(),
            auth = FirebaseAuth.getInstance(),
            firestore = com.google.firebase.firestore.FirebaseFirestore.getInstance()
        )
    }

    val viewModelFactory = remember { UserViewModelFactory(repository) }
    val signUpViewModelFactory = remember { SignUpViewModelFactory(repository) }

    //If you have an EggRepository and EggCollectionViewModel:
    val eggRepository = remember { EggRepository(database.eggCollectionDao()) }
    val eggCollectionViewModelFactory = remember { EggCollectionViewModelFactory(eggRepository) }

    //initialise the FlockArrivalRepository and its Factory
    val flockArrivalRepository = remember { FlockArrivalRepository(database.flockArrivalDao()) }
    val flockArrivalViewModelFactory =
        remember { FlockArrivalViewModelFactory(flockArrivalRepository) }


    //Initialise the FeedRepository and its Factory
    val feedRequirementsRepository = remember {
        FeedRequirementsRepository(database.feedRequirementDao())
    }

    val feedRequirementsViewModelFactory = remember {
        FeedRequirementsViewModelFactory(feedRequirementsRepository)
    }

    //Screen Navigation

    NavHost(navController = navController, startDestination = "splash") {

        composable("splash") {
            SplashScreen(navController)

        }


        // Login/Signup Screen (parent for shared ViewModel)
        composable("login_signup") { backStackEntry ->
            val viewModel: UserViewModel = viewModel(
                viewModelStoreOwner = backStackEntry,
                factory = viewModelFactory
            )
            LoginSignUp(
                navController = navController,
                viewModel = viewModel,
                onLoginSuccess = {
                    // After successful login, go to dashboard
                    navController.navigate("dashboard") {
                        popUpTo("login_signup") { inclusive = true }
                    }
                }
            )
        }

        // Dashboard Screen
        composable("dashboard") { backStackEntry ->
            val userViewModel: UserViewModel = viewModel(
                viewModelStoreOwner = backStackEntry,
                factory = viewModelFactory
            )
            val eggViewModel: EggCollectionViewModel = viewModel(
                viewModelStoreOwner = backStackEntry,
                factory = eggCollectionViewModelFactory
            )
            val flockArrivalViewModel: FlockArrivalViewModel = viewModel(
                viewModelStoreOwner = backStackEntry,
                factory = flockArrivalViewModelFactory
            )

            Dashboard(
                navController,
                userViewModel,
                eggViewModel,
                flockArrivalViewModel = flockArrivalViewModel,

                )
        }


        // Signup Screen (shares ViewModel with login_signup if needed, or create a new one if for registration)
        composable("signup") { backStackEntry ->
            val viewModel: SignUpViewModel = viewModel(
                viewModelStoreOwner = backStackEntry,
                factory = signUpViewModelFactory
            )
            SignUp(navController, viewModel)
        }

        //Egg Collection Screens

        composable("egg_collection_home") {
            EggCollectionHome(navController)
        }
        composable("egg_collection_form") { backStackEntry ->
            //If you want a ViewModel, you can create and pass it here
            val viewModel: EggCollectionViewModel = viewModel(
                viewModelStoreOwner = backStackEntry,
                factory = eggCollectionViewModelFactory
            )
            EggCollectionEntry(navController, viewModel = viewModel)
        }
        composable("egg_collection_chart") { backStackEntry ->
            val viewModel: EggCollectionViewModel = viewModel(
                viewModelStoreOwner = backStackEntry,
                factory = eggCollectionViewModelFactory
            )
            EggCollectionGraph(navController, viewModel = viewModel)
        }
        composable("display_egg_collection") { backStackEntry ->
            val viewModel: EggCollectionViewModel = viewModel(
                viewModelStoreOwner = backStackEntry,
                factory = eggCollectionViewModelFactory
            )
            DisplayEggCollection(navController, viewModel)
        }
        composable("flock_arrival_form") { backStackEntry ->
            val viewModel: FlockArrivalViewModel = viewModel(
                viewModelStoreOwner = backStackEntry,
                factory = flockArrivalViewModelFactory
            )
            FlockArrivalForm(
                navController = navController,
                viewModel = viewModel
            )

        }
        composable("display_flock_docs") { backStackEntry ->
            val viewModel: FlockArrivalViewModel = viewModel(
                viewModelStoreOwner = backStackEntry,
                factory = flockArrivalViewModelFactory
            )
            DisplayFlockDocs(navController, viewModel)
        }
        // Broiler Projection Form Route
        composable("broiler_projection_form") {
            BroilerProjectionForm(navController) { chicks, feed, mortality, price ->
                // You can handle the projection result here (show dialog, save, etc.)
                println("Projection: $chicks chicks, feed $feed, mortality $mortality, price $price")
            }


        }


        composable("feeds_home") {
            FeedsHome(navController)
        }
        composable("feed_calculation_home") {
            FeedCalculationHome(navController)
        }

        composable("financials_home") {
            FinancialsHome(navController)
        }



        composable("calc_feed_cost_bags") { backStackEntry ->
            val viewModel: FeedRequirementsViewModel = viewModel(
                viewModelStoreOwner = backStackEntry,
                factory = feedRequirementsViewModelFactory
            )
            FeedCostCalculatorBags(navController, viewModel)
            {
                navController.popBackStack()
            }
        }

        composable("calc_feed_cost_kg") { backStackEntry ->
            val viewModel: FeedRequirementsViewModel = viewModel(
                viewModelStoreOwner = backStackEntry,
                factory = feedRequirementsViewModelFactory
            )
            FeedCostCalculatorKg(navController, viewModel)
            {
                navController.popBackStack()
            }
        }
    }
}

//When bottom bar is implemented in Navigation files are as follows:
/*
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val context = LocalContext.current.applicationContext
    val database = remember { BirdFeatherDatabase.getDatabase(context) }

    val repository = remember {
        UserRepository(
            userDao = database.userDao(),
            firebaseAuth = FirebaseAuth.getInstance()
        )
    }

    val viewModelFactory = remember { UserViewModelFactory(repository) }
    val signUpViewModelFactory = remember { SignUpViewModelFactory(repository) }

    val eggRepository = remember { EggRepository(database.eggCollectionDao()) }
    val eggCollectionViewModelFactory = remember { EggCollectionViewModelFactory(eggRepository) }

    val flockArrivalRepository = remember { FlockArrivalRepository(database.flockArrivalDao()) }
    val flockArrivalViewModelFactory = remember { FlockArrivalViewModelFactory(flockArrivalRepository) }

    val feedRequirementsRepository = remember {
        FeedRequirementsRepository(database.feedRequirementDao())
    }
    val feedRequirementsViewModelFactory = remember {
        FeedRequirementsViewModelFactory(feedRequirementsRepository)
    }

    // 🧠 Determine current route for conditional bottom bar
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = currentBackStackEntry?.destination?.route

    val showBottomBar = currentRoute == "dashboard"

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(navController)
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("splash") {
                SplashScreen(navController)
                LaunchedEffect(Unit) {
                    delay(400)
                    navController.navigate("login_signup") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            }

            composable("login_signup") { backStackEntry ->
                val viewModel: UserViewModel = viewModel(
                    viewModelStoreOwner = backStackEntry,
                    factory = viewModelFactory
                )
                LoginSignUp(
                    navController = navController,
                    viewModel = viewModel,
                    onLoginSuccess = {
                        navController.navigate("dashboard") {
                            popUpTo("login_signup") { inclusive = true }
                        }
                    }
                )
            }

            composable("signup") { backStackEntry ->
                val viewModel: SignUpViewModel = viewModel(
                    viewModelStoreOwner = backStackEntry,
                    factory = signUpViewModelFactory
                )
                SignUp(navController, viewModel)
            }

            composable("dashboard") { backStackEntry ->
                val userViewModel: UserViewModel = viewModel(
                    viewModelStoreOwner = backStackEntry,
                    factory = viewModelFactory
                )
                val eggViewModel: EggCollectionViewModel = viewModel(
                    viewModelStoreOwner = backStackEntry,
                    factory = eggCollectionViewModelFactory
                )
                val flockArrivalViewModel: FlockArrivalViewModel = viewModel(
                    viewModelStoreOwner = backStackEntry,
                    factory = flockArrivalViewModelFactory
                )

                Dashboard(
                    navController,
                    userViewModel,
                    eggViewModel,
                    flockArrivalViewModel
                )
            }

            composable("db_verify") {
                DatabaseVerifyScreen(navController = navController)
            }

            composable("egg_collection_home") {
                EggCollectionHome(navController)
            }

            composable("egg_collection_form") { backStackEntry ->
                val viewModel: EggCollectionViewModel = viewModel(
                    viewModelStoreOwner = backStackEntry,
                    factory = eggCollectionViewModelFactory
                )
                EggCollectionEntry(navController, viewModel = viewModel)
            }

            composable("egg_collection_chart") { backStackEntry ->
                val viewModel: EggCollectionViewModel = viewModel(
                    viewModelStoreOwner = backStackEntry,
                    factory = eggCollectionViewModelFactory
                )
                EggCollectionGraph(navController, viewModel)
            }

            composable("display_egg_collection") { backStackEntry ->
                val viewModel: EggCollectionViewModel = viewModel(
                    viewModelStoreOwner = backStackEntry,
                    factory = eggCollectionViewModelFactory
                )
                DisplayEggCollection(navController, viewModel)
            }

            composable("flock_arrival_form") { backStackEntry ->
                val viewModel: FlockArrivalViewModel = viewModel(
                    viewModelStoreOwner = backStackEntry,
                    factory = flockArrivalViewModelFactory
                )
                FlockArrivalForm(navController = navController, viewModel = viewModel)
            }

            composable("display_flock_docs") { backStackEntry ->
                val viewModel: FlockArrivalViewModel = viewModel(
                    viewModelStoreOwner = backStackEntry,
                    factory = flockArrivalViewModelFactory
                )
                DisplayFlockDocs(navController, viewModel)
            }

            composable("broiler_projection_form") {
                BroilerProjectionForm(navController) { chicks, feed, mortality, price ->
                    println("Projection: $chicks chicks, feed $feed, mortality $mortality, price $price")
                }
            }

            composable("feeds_home") {
                FeedsHome(navController)
            }

            composable("feed_calculation_home") {
                FeedCalculationHome(navController)
            }

            composable("calc_feed_cost_bags") { backStackEntry ->
                val viewModel: FeedRequirementsViewModel = viewModel(
                    viewModelStoreOwner = backStackEntry,
                    factory = feedRequirementsViewModelFactory
                )
                FeedCostCalculatorBags(navController, viewModel) {
                    navController.popBackStack()
                }
            }

            composable("calc_feed_cost_kg") { backStackEntry ->
                val viewModel: FeedRequirementsViewModel = viewModel(
                    viewModelStoreOwner = backStackEntry,
                    factory = feedRequirementsViewModelFactory
                )
                FeedCostCalculatorKg(navController, viewModel) {
                    navController.popBackStack()
                }
            }
        }
    }
}
*/


