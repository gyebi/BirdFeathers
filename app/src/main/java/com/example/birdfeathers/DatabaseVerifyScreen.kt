package com.example.birdfeathers
/*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.birdfeathers.BirdFeatherDatabase
import com.example.birdfeathers.entity.LocalUser
import com.example.birdfeathers.ui.theme.BirdFeathersTheme
import com.example.birdfeathers.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun DatabaseVerifyScreen(navController: NavHostController,  viewModel: UserViewModel = viewModel()) {
    val context = LocalContext.current
    val db = remember { BirdFeatherDatabase.getDatabase(context) }
    val coroutineScope = rememberCoroutineScope()

    var users by remember { mutableStateOf<List<LocalUser>>(emptyList()) }

    LaunchedEffect(Unit) {
        users = db.userDao().getAllUsers()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Stored Users (Room DB)",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        users.forEach { user ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Text("Email: ${user.email}", fontWeight = FontWeight.Bold)
                    Text("Name: ${user.displayName}")
                    //Text("Hashed Password: ${user.hashedPassword}", fontSize = 12.sp, color = Color.Gray)
                }
            }


        }

        Spacer(modifier = Modifier.height(36.dp))

        Button(
            onClick = {// Example: Add a dummy user (must be inside a coroutine)
                coroutineScope.launch {
                    db.userDao().insert(
                        uid = '',
                        email = doc.getString("email"),
                        displayName = doc.getString("displayName"),
                        phone = doc.getString("phone"),
                        photoUrl = doc.getString("photoUrl")
                    )
                    users = db.userDao().getAllUsers() // refresh list
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text( "Add Dummy User")
        }


        if (users.isEmpty()) {
            Text("No users found.", color = Color.Red)
        }


    }
}


@Preview(showBackground = true)
@Composable
fun PreviewDebugUserList() {
    BirdFeathersTheme {
        DatabaseVerifyScreen(navController = NavHostController(LocalContext.current))
    }
}

 */