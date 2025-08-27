package com.example.birdfeathers.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.birdfeathers.entity.LocalUser
import com.example.birdfeathers.domain.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed class SignUpState {
    object Idle : SignUpState()
    object Loading : SignUpState()
    object Success : SignUpState() // you can make this `data class Success(val uid: String)` if you want
    data class Error(val message: String) : SignUpState()
}

class SignUpViewModel(
    private val repository: UserRepository,
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {

    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Idle)
    val signUpState: StateFlow<SignUpState> = _signUpState

    fun registerUser(email: String, password: String, name: String) {
        if (name.isBlank() || !email.contains("@") || password.length < 6) {
            _signUpState.value = SignUpState.Error("Fill in all the Fields")
            return
        }

        _signUpState.value = SignUpState.Loading
        viewModelScope.launch {
            try {
                val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                val firebaseUser = result.user ?: firebaseAuth.currentUser
                requireNotNull(firebaseUser) { "User not available after sign up." }

                // KTX DSL for profile updates ✅
                val profileUpdates = userProfileChangeRequest {
                    displayName = name
                }
                firebaseUser.updateProfile(profileUpdates).await()

                // 1) (Recommended) Upsert minimal profile to Firestore (remote SoT)
                repository.upsertRemoteUser(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email,
                    displayName = firebaseUser.displayName
                )

                // 2) Cache to Room (local read source)
                repository.upsertLocalUser(
                    LocalUser(
                        uid = firebaseUser.uid,
                        email = firebaseUser.email,
                        displayName = firebaseUser.displayName,
                        phone = firebaseUser.phoneNumber,
                        photoUrl = firebaseUser.photoUrl?.toString()
                    )
                )

                _signUpState.value = SignUpState.Success
            } catch (e: Exception) {
                _signUpState.value = SignUpState.Error(e.message ?: "Registration failed")
            }
        }
    }
}

class SignUpViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SignUpViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
