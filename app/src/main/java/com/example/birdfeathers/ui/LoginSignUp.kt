/*package com.example.birdfeathers


import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.FirebaseAuth
import android.R.attr.fontWeight
import android.R.attr.onClick
import android.R.attr.password
import android.R.attr.showText
import android.R.attr.text
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SearchBarDefaults.colors
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
//import com.example.birdfeathers.FirebaseAuthManager.AuthResponse
import com.example.birdfeathers.ui.theme.BirdFeathersTheme
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.ActivityResult
//import android.widget.Toast
//import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth



@Composable

fun LoginSignUp(
    //onContinueClick: () -> Unit,
    navController: NavHostController,
    viewModel: UserViewModel = viewModel(),
    onLoginSuccess: () -> Unit

) {
    var emailOrPhone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var showPassword by remember { mutableStateOf(false) }

    var forgotPasswordDialogBox by remember { mutableStateOf(false) }
    var resetEmail by remember { mutableStateOf("") }


    val context = LocalContext.current
    val authManager = remember { FirebaseAuthManager(context) }
    val coroutineScope = rememberCoroutineScope()

    val firestoreManager = remember { FirestoreUserManager(context) }
    var uid = remember { "" }

    val loginState by viewModel.loginState.collectAsState()

    // 1) Launcher used when there are no saved Google credentials on device
    val addAccountLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { /* no-op */ }


    // Handle navigation on login success
    LaunchedEffect(loginState) {
        if (loginState is LoginState.Success || loginState is LoginState.OfflineSuccess) {
            navController.navigate("dashboard") {
                popUpTo("login_signup") { inclusive = true }
            }
        }
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    )
    {
        Spacer(modifier = Modifier.height(100.dp))

        // Logo placeholder
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(Color.Black, shape = CircleShape)

        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Sign In ",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 30.sp

        )

        Spacer(modifier = Modifier.height(16.dp))

        // Show login status
        when (loginState) {
            is LoginState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
            is LoginState.Error -> {
                val error = (loginState as LoginState.Error).message
                Text(
                    text = error,
                    color = Color.Red,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            is LoginState.OfflineSuccess -> {
                // Optional: You could show a banner here ("Logged in offline"), or just rely on navigation
                Text(
                    text = "Logged in (offline mode)",
                    color = Color(0xFF388E3C),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            // For LoginState.Success, you already handle navigation with LaunchedEffect
            else -> { /* No message */ }
        }


        // Email TextField
        OutlinedTextField(
            value = emailOrPhone,
            onValueChange = { emailOrPhone = it },

            placeholder = { Text("Email or Phone") },
            leadingIcon = {
                Icon(Icons.Rounded.Email, contentDescription = null)
            },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)

        )

        Spacer(modifier = Modifier.height(16.dp))


        // Password TextField
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Password") },
            leadingIcon = {
                Icon(Icons.Rounded.Lock, contentDescription = null)
            },
            trailingIcon = {
                val icon = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                val desc = if (showPassword) "Hide password" else "Show password"
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(imageVector = icon, contentDescription = desc)
                }
            },
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Continue Button
        OutlinedButton(
            onClick = {viewModel.login(emailOrPhone, password) },
            enabled = loginState !is LoginState.Loading,
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(2.dp,LightGray)

        )

        {
            Text(text = "Continue",
                color = Color.Black,
                fontSize = 25.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if(forgotPasswordDialogBox) {

            AlertDialog(
                title = { Text("Reset Password") },
                text = {
                    OutlinedTextField(
                        value = resetEmail,
                        onValueChange = { resetEmail = it },
                        label = { Text("Enter your email") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }, confirmButton = {
                    TextButton(onClick = {
                        if (resetEmail.isNotBlank()) {
                            Firebase.auth.sendPasswordResetEmail(resetEmail)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(
                                            context, "Check your email to reset password",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        forgotPasswordDialogBox = false
                                    } else {
                                        Toast.makeText(
                                            context, "Registered email not found",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        } else {
                            Toast.makeText(
                                context, "Please enter your registered email",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }) { Text("Reset") }
                }, dismissButton = {
                    TextButton(onClick = { forgotPasswordDialogBox = false }) {
                        Text(text = "Cancel")
                    }
                }, onDismissRequest = { forgotPasswordDialogBox = false }
            )
        }



        TextButton(onClick = { forgotPasswordDialogBox = true }) {
            Text(text = "Forgot Password?",
                color = Color.Black,
                fontSize = 18.sp,
                textAlign = TextAlign.Right
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        Text( text = "or",
            fontSize = 30.sp,
            textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(16.dp))


        //SocialButton("Continue with Google", icon = Icons.Default.AccountCircle)
        OutlinedButton(
            onClick = {
                GoogleSignInUtils.signInWithGoogle(
                    context = context,
                    scope = coroutineScope,
                    launcher = addAccountLauncher, // used only when NoCredentialException
                    onLoginSuccess  = { user ->
                        // persist session if you want (DataStore/SharedPref). Firebase already keeps session.
                        Toast.makeText(context, "Welcome ${user.displayName ?: ""}", Toast.LENGTH_SHORT).show()
                        navController.navigate("dashboard") {
                            popUpTo("login_signup") { inclusive = true }
                        }
                    },
                    onError = { t ->
                        Toast.makeText(context, t.message ?: "Google sign-in failed", Toast.LENGTH_LONG).show()
                    }
                )
                      },

            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(2.dp,LightGray)
        )
        {
            Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
            ) {
            Image(
                painter = painterResource(id = R.drawable.google_icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Text(text = "Continue with Google",
                color = Color.Black,
                fontSize = 18.sp,
                textAlign = TextAlign.Left
            )
        }
        }

        Spacer(modifier = Modifier.height(16.dp))

        /*
        // SocialButton("Continue with Facebook", icon = Icons.Default.Face)

        OutlinedButton(
            onClick = {""},
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(2.dp,LightGray)

        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.facebook_logo),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Text(text = "Continue with Facebook",
                    color = Color.Blue,
                    fontSize = 25.sp,
                    textAlign = TextAlign.Left
                )
            }
        }
*/
        Spacer(modifier = Modifier.height(16.dp))


        Text(
            text = "Need help signing in? Register !",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp,
            modifier = Modifier.clickable {
                navController.navigate("signup") }
        )

        Spacer(modifier = Modifier.height(20.dp))

        /*
        Text(
            text = "By signing up, you’re creating a BirdFeathers account and agree to our Terms and Privacy Policy.",
            fontSize = 12.sp,
            color = Color.Gray,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

         */
    }
}

@Preview (showBackground = true)
@Composable
private fun LoginSignUpPreview() {
    BirdFeathersTheme {
        LoginSignUp(
            navController = rememberNavController(),
            onLoginSuccess = { /* Handle login success */ }
        )
    }
}

*/

package com.example.birdfeathers.ui

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.birdfeathers.GoogleSignInUtils
import com.example.birdfeathers.R
import com.example.birdfeathers.app.theme.FarmGreen
import com.example.birdfeathers.app.theme.RichEarth
import com.example.birdfeathers.ui.theme.BirdFeathersTheme
import com.example.birdfeathers.viewmodel.LoginState
import com.example.birdfeathers.viewmodel.UserViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun LoginSignUp(
    navController: NavHostController,
    viewModel: UserViewModel = viewModel(),
    onLoginSuccess: () -> Unit = {}
) {
    var emailOrPhone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var forgotPasswordDialogBox by remember { mutableStateOf(false) }
    var resetEmail by remember { mutableStateOf("") }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val loginState by viewModel.loginState.collectAsState()

    // Used for adding a Google account when there’s no stored credential
    val addAccountLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { /* No-op */ }

    // Handle navigation after successful login
    LaunchedEffect(loginState) {
        if (loginState is LoginState.Success || loginState is LoginState.OfflineSuccess) {
            navController.navigate("dashboard") {
                popUpTo("login_signup") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(100.dp))

        // Logo placeholder
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(Color.Black, shape = CircleShape)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Sign In",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Show login status
        when (loginState) {
            is LoginState.Loading -> CircularProgressIndicator(modifier = Modifier.padding(vertical = 16.dp))
            is LoginState.Error -> Text(
                text = (loginState as LoginState.Error).message,
                color = Color.Red,
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            is LoginState.OfflineSuccess -> Text(
                text = "Logged in (offline mode)",
                color = Color(0xFF388E3C),
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            else -> {}
        }

        // Email TextField
        OutlinedTextField(
            value = emailOrPhone,
            onValueChange = { emailOrPhone = it },
            placeholder = { Text("Email or Phone") },
            leadingIcon = { Icon(Icons.Rounded.Email, contentDescription = null) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password TextField
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Password") },
            leadingIcon = { Icon(Icons.Rounded.Lock, contentDescription = null) },
            trailingIcon = {
                val icon = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                val desc = if (showPassword) "Hide password" else "Show password"
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(imageVector = icon, contentDescription = desc)
                }
            },
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Continue Button
        OutlinedButton(
            onClick = { viewModel.login(emailOrPhone, password) },
            enabled = loginState !is LoginState.Loading,
            colors = ButtonDefaults.buttonColors(containerColor = FarmGreen),            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(2.dp, LightGray)
        ) {
            Text("Continue", color = Color.Black, fontSize = 20.sp, textAlign = TextAlign.Center)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Forgot password dialog
        if (forgotPasswordDialogBox) {
            AlertDialog(
                title = { Text("Reset Password") },
                text = {
                    OutlinedTextField(
                        value = resetEmail,
                        onValueChange = { resetEmail = it },
                        label = { Text("Enter your email") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (resetEmail.isNotBlank()) {
                            Firebase.auth.sendPasswordResetEmail(resetEmail)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(
                                            context, "Check your email to reset password",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        forgotPasswordDialogBox = false
                                    } else {
                                        Toast.makeText(
                                            context, "Registered email not found",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        } else {
                            Toast.makeText(
                                context, "Please enter your registered email",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }) { Text("Reset") }
                },
                dismissButton = {
                    TextButton(onClick = { forgotPasswordDialogBox = false }) {
                        Text("Cancel")
                    }
                },
                onDismissRequest = { forgotPasswordDialogBox = false }
            )
        }

        TextButton(onClick = { forgotPasswordDialogBox = true }) {
            Text("Forgot Password?", color = Color.Black, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("or", fontSize = 30.sp, textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(16.dp))

        // Google Sign-In Button
        OutlinedButton(
            onClick = {
                GoogleSignInUtils.signInWithGoogle(
                    context = context,
                    scope = coroutineScope,
                    launcher = addAccountLauncher,
                    onLoginSuccess = { user ->
                        Toast.makeText(context, "Welcome ${user.displayName ?: ""}", Toast.LENGTH_SHORT).show()
                        navController.navigate("dashboard") {
                            popUpTo("login_signup") { inclusive = true }
                        }
                    },
                    onError = { error ->
                        Toast.makeText(
                            context,
                            error.message ?: "Google sign-in failed",
                            Toast.LENGTH_LONG).show()
                    }
                )
            },

            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(2.dp, LightGray)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google_icon),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Continue with Google", color = Color.Black, fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sign-up text
        Text(
            text = "Need help signing in? Register!",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp,
            modifier = Modifier.clickable {
                navController.navigate("signup")
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginSignUpPreview() {
    BirdFeathersTheme {
        LoginSignUp(navController = rememberNavController())
    }
}

