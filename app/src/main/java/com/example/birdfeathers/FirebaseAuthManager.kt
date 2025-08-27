package com.example.birdfeathers

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.security.MessageDigest
import java.util.UUID

class FirebaseAuthManager(private val context: Context) {

    private val auth = Firebase.auth

    fun createAccountWithPhoneEmail(emailPhone: String, password: String): Flow<AuthResponse> =
        callbackFlow {
            auth.createUserWithEmailAndPassword(emailPhone, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(AuthResponse.Success)
                    } else {
                        trySend(
                            AuthResponse.Error(
                                task.exception?.message ?: "Registration failed"
                            )
                        )
                    }
                }
            awaitClose()
        }

    fun loginWithPhoneEmail(emailPhone: String, password: String): Flow<AuthResponse> =
        callbackFlow {
            auth.signInWithEmailAndPassword(emailPhone, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(AuthResponse.Success)
                    } else {
                        trySend(AuthResponse.Error(task.exception?.message ?: "Login failed"))
                    }
                }
            awaitClose()
        }

    private fun createNonce(): String {
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)

        return digest.joinToString("") { "%02x".format(it) }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    /*fun signInWithGoogle(): Flow<AuthResponse> = callbackFlow {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(context.getString(R.string.web_client_id))
            .setAutoSelectEnabled(false)
            .setNonce(createNonce())
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        try {
            val credentialManager = CredentialManager.create(context)
            val result = credentialManager.getCredential(
                context = context,
                request = request
            )

            val credential = result.credential
            if (credential is CustomCredential) {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                        val firebaseCredential = GoogleAuthProvider.getCredential(
                            googleIdTokenCredential.idToken, null
                        )
                        auth.signInWithCredential(firebaseCredential)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    trySend(AuthResponse.Success)
                                } else {
                                    trySend(AuthResponse.Error(it.exception?.message ?: " "))
                                }
                            }

                    } catch (e: GoogleIdTokenParsingException) {
                        trySend(AuthResponse.Error(e.message ?: "GoogleIdTokenParsingException"))
                    }
                }
            }
        } catch (e: Exception) {
            trySend(AuthResponse.Error(e.message ?: "Google Sign-in error"))
        }
        awaitClose()
    }

    private fun GetCredentialRequest.Builder.addCredentialOption(option: GetGoogleIdOption) {}
}
*/
// Put this in a separate file if you want to use it elsewhere
    sealed interface AuthResponse {
        object Success : AuthResponse
        data class Error(val message: String) : AuthResponse
    }
}
