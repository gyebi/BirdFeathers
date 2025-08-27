package com.example.birdfeathers.ui


import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.birdfeathers.viewmodel.UserViewModel

@Composable
fun LoginPassword(
    //onContinueClick: () -> Unit,
    navController: NavHostController,
    viewModel: UserViewModel

) {

    Log.d("LoginPassword", "Log in Password composable loaded")

    //var emailOrPhone = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(300.dp))

        // Logo placeholder
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(Color.Black, shape = CircleShape)

        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Enter Password",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 50.sp


        )

        Spacer(modifier = Modifier.height(36.dp))

        // TextField
        OutlinedTextField(
            value = password.value,
            onValueChange ={
                password.value = it
                //viewModel.updateEmailOrPhone(it)
            } ,
            label = {Text("Password") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Continue Button
        Button(
            onClick = {
                navController.navigate(route = "dashboard")
                //if (viewModel.validateEmailOrPhone()) {
                //onContinueClick()
                //}
            },
            colors = ButtonDefaults.buttonColors(containerColor = LightGray),
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(2.dp,LightGray)

        ) {
            Text(text = "Continue",
                color = Color.Black,
                fontSize = 25.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text( text = "or",
            fontSize = 30.sp,
            textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(24.dp))


        // Social Buttons
        //SocialButton("Continue with Google", icon = Icons.Default.AccountCircle)
        Button(
            onClick = { " " },
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(2.dp,LightGray)

        ) {
            Text(text = "Continue with Google",
                color = Color.Black,
                fontSize = 25.sp,
                textAlign = TextAlign.Left
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        //SocialButton("Continue with Apple", icon = Icons.Default.Apple)
        // SocialButton("Continue with Facebook", icon = Icons.Default.Face)

        Button(
            onClick = {""},
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(2.dp,LightGray)

        ) {
            Text(text = "Continue with Facebook",
                color = Color.Blue,
                fontSize = 25.sp,
                textAlign = TextAlign.Left


            )
        }

        Spacer(modifier = Modifier.height(50.dp))


        Text(
            text = "Need help signing in?",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 25.sp
        )

        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "By signing up, you’re creating a BirdFeathers account and agree to our Terms and Privacy Policy.",
            fontSize = 12.sp,
            color = Color.Gray,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}


