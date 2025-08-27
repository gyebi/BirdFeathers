package com.example.birdfeathers.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FirebaseUserViewModel : ViewModel() {
    private val _user = MutableStateFlow<FirebaseUser?>(FirebaseAuth.getInstance().currentUser)
    val user: StateFlow<FirebaseUser?> = _user

    fun refresh() {
        _user.value = FirebaseAuth.getInstance().currentUser
    }

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
        _user.value = null
    }
}
