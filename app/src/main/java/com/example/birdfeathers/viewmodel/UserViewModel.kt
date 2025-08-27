package com.example.birdfeathers.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.birdfeathers.domain.UserRepository
import com.example.birdfeathers.entity.LocalUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val user: FirebaseUser) : LoginState()
    data class OfflineSuccess(val user: LocalUser) : LoginState()
    data class Error(val message: String) : LoginState()
}

class UserViewModel(
    private val repository: UserRepository,
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    private val _currentUser = MutableStateFlow<LocalUser?>(null)
    val currentUser: StateFlow<LocalUser?> = _currentUser

    fun login(email: String, password: String) {
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            try {
                val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                val fbUser = result.user ?: firebaseAuth.currentUser
                if (fbUser == null) {
                    _loginState.value = LoginState.Error("User not available after sign in.")
                    return@launch
                }

                // Ensure we have a local record (create if missing) — by UID
                val local: LocalUser = repository.getUserByUidFromRoom(fbUser.uid)
                    ?: LocalUser(
                        uid = fbUser.uid,
                        email = fbUser.email,
                        displayName = fbUser.displayName,
                        phone = fbUser.phoneNumber,
                        photoUrl = fbUser.photoUrl?.toString()
                    ).also { repository.upsertLocalUser(it) }

                _currentUser.value = local
                _loginState.value = LoginState.Success(fbUser)

            } catch (e: Exception) {
                // Offline fallback only if a Firebase session exists
                val fbUser = firebaseAuth.currentUser
                if (fbUser != null) {
                    val local = repository.getUserByUidFromRoom(fbUser.uid)
                        ?: LocalUser(
                            uid = fbUser.uid,
                            email = fbUser.email,
                            displayName = fbUser.displayName,
                            phone = fbUser.phoneNumber,
                            photoUrl = fbUser.photoUrl?.toString()
                        ).also { repository.upsertLocalUser(it) }

                    //_currentUser.value = local
                    _loginState.value = LoginState.OfflineSuccess(local)
                } else {
                    _loginState.value = LoginState.Error("Offline and no saved session yet.")
                }
            }
        }
    }

    private fun isEmailValid(email: String) =
        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun resetLoginState() {
        _loginState.value = LoginState.Idle
    }
}

    //private fun hashPassword(password: String): String = password.reversed() // demo only}

class UserViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
