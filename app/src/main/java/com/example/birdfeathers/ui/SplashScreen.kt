package com.example.birdfeathers.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.birdfeathers.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navController: NavHostController) {
    val textAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        textAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 3000)
        )
    }
    // Decide where to go (after a short splash)
    LaunchedEffect(Unit) {
        delay(1200) // keep in sync with the animation above
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            navController.navigate("dashboard") {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            navController.navigate("login_signup") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isSystemInDarkTheme()) Color.DarkGray else Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.animals_onfarm_chicken),
            contentDescription = "Intro Pic",
            modifier = Modifier.size(500.dp)
        )

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = "The Chicken Farmer's Friend",
            fontSize = 50.sp,
            modifier = Modifier
                .alpha(textAlpha.value),
            fontWeight = FontWeight.ExtraBold

        )
    }
}


@Composable
fun LoaderAnimation(modifier: Modifier, anim :Int){

    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(anim))

    LottieAnimation(
        composition = composition, iterations = LottieConstants.IterateForever,
        modifier = modifier
    )
}

