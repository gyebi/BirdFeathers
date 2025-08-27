package com.example.birdfeathers

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.birdfeathers.domain.UserRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await



class LoginViewModel(
    private val repository: UserRepository
): ViewModel() {
    var emailOrPhone by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    var loginStatus by mutableStateOf<String?>(null)

    fun updateEmailOrPhone(newEmailOrPhone: String) {
        emailOrPhone = newEmailOrPhone

    }

    fun updatePassword(newPassword: String) {
        password = newPassword

    }


    fun login(
        emailOrPhone: String,
        password: String,
        context: Context,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val auth = Firebase.auth
            try {
                auth.signInWithEmailAndPassword(emailOrPhone, password).await()
                showToast(context, "Logged in via Firebase")
                onSuccess()

            } catch (e: Exception) {
                // Fallback to Room
                val user = repository.getUserByEmail(emailOrPhone)
                val hashedInput = password.hashCode().toString()

                if (user != null && user.hashedPassword == hashedInput) {
                    showToast(context, "Logged in offline")
                    onSuccess()
                } else {
                    showToast(context, "Login failed, Check email or password")
                    onFailure()
                }
            }
        }
    }

    private fun showToast(context: Context, message: String) {
        viewModelScope.launch(Dispatchers.Main) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}