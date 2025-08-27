package com.example.birdfeathers.ui

import android.widget.Toast
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.birdfeathers.viewmodel.SignUpState
import com.example.birdfeathers.viewmodel.SignUpViewModel

@Composable

fun SignUp(navController: NavHostController,
           viewModel: SignUpViewModel
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

    val context = LocalContext.current

    //observe sign up state
    val signUpState by viewModel.signUpState.collectAsState()

    //Handle navigation on success
    if (signUpState is SignUpState.Success) {
        //use launched effect so navigation / toast only happens once

        LaunchedEffect(Unit) {
            Toast.makeText(context, "Registration successful! Please log in", Toast.LENGTH_SHORT)
                .show()
            navController.navigate("login_signup") {
                popUpTo("signup") { inclusive = true }
            }
        }
    }

//    fun hashPassword(password: String): String {
//        return password.hashCode().toString() // basic hash for now
//    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 48.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(200.dp))

        // Logo placeholder
        Box(
            modifier = Modifier
                .size(60.dp)

                .background(Color.Black, shape = CircleShape)
                .align(Alignment.CenterHorizontally)

        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = " Sign Up / Register",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 30.sp

        )

        Spacer(modifier = Modifier.height(36.dp))

        when(signUpState) {
            is SignUpState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            is SignUpState.Error -> {
                Text(
                    text = (signUpState as SignUpState.Error).message,
                    color = Color.Red,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            else -> {}
        }


        // Email TextField
        OutlinedTextField(
            value = email,
            onValueChange = { newValue ->
                email = newValue
            },

            label = { Text("Email ") },
            leadingIcon = {
                Icon(Icons.Rounded.Email, contentDescription = null)
            },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
        )

        Spacer(modifier = Modifier.height(24.dp))


        // Password TextField
        OutlinedTextField(
            value = password,
            onValueChange = { newValue ->
                password = newValue},
            label = { Text("Password") },
            leadingIcon = {
                Icon(Icons.Rounded.Lock, contentDescription = null)
            },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,

            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

       // Name  TextField
        OutlinedTextField(
            value = name,
            onValueChange = { newValue ->
                name = newValue

            },
            placeholder = { Text("Name") },
            leadingIcon = {
                Icon(Icons.Rounded.Face, contentDescription = null)
            },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Continue Button
        OutlinedButton(
            onClick = {

                if (email.isBlank() || password.isBlank() || name.isBlank()) {
                    Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                    return@OutlinedButton
                } else {
                    viewModel.registerUser(email, password, name)
                }
            },

/*
               Firebase.auth.createUserWithEmailAndPassword(email, password)
                   .addOnCompleteListener { task ->
                       if (task.isSuccessful) {
                           // Registration successful, save to RoomDatabase
                           CoroutineScope(Dispatchers.IO).launch {
                               val hashed = hashPassword(password)
                               val localUser = LocalUser(
                                   email= email, hashedPassword =hashed, name = name)

                               val db = BirdFeatherDatabase.getDatabase(context)
                               db.userDao().insert(localUser)

                                   val userFromDb = db.userDao().getAllUsers()
                               Log.d("ROOM_CHECK", "User stored: $userFromDb")

                               kotlinx.coroutines.withContext(Dispatchers.Main) {
                                   Toast.makeText(context, "Registration successful! Please log in", Toast.LENGTH_SHORT).show()
                                   navController.navigate("login_signup")
                               }

                           }

                       }
                       else {
                           // Registration failed, display an error message
                           val errorMessage = task.exception?.message ?: "Registration failed"
                           Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()

                           navController.navigate("signup")
                       }
                       }

            },
*/
            enabled = signUpState !is SignUpState.Loading,
            colors = ButtonDefaults.buttonColors(containerColor = LightGray),
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(2.dp, LightGray)
        ) {
            Text(
                text = "Sign Up / Register",
                color = Color.Black,
                fontSize = 25.sp,
                textAlign = TextAlign.Center
            )
        }

    }
}

   /* @Preview (showBackground = true)
    @Composable
    private fun Screen.SignUp() {
        BirdFeathersTheme {
            SignUp(navController = NavHostController(LocalContext.current))
        }
    }

    */