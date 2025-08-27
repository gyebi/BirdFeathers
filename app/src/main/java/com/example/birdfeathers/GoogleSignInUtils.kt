
package com.example.birdfeathers

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await



object GoogleSignInUtils {

    /**
     * Attempts Google Sign-In using Credential Manager + Google ID token.
     * On success -> calls onLoginSuccess(user)
     * If no credential available -> launches Add Account intent via launcher (if provided)
     */
    fun signInWithGoogle(
        context: Context,
        scope: CoroutineScope,
        launcher: ManagedActivityResultLauncher<Intent, ActivityResult>? = null,
        onLoginSuccess: (FirebaseUser) -> Unit,
        onError: (Throwable) -> Unit = {}
    ) {
        val credentialManager = CredentialManager.create(context)
        val request = buildGoogleIdRequest(context)


        scope.launch {
            try {
                // Retrieve a credential (passkey/password/federated)
                val result = credentialManager.getCredential(context, request)
                val credential = result.credential

                if (
                    credential is CustomCredential &&
                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) {
                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(credential.data)
                    val authCredential =
                        GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                    val user = Firebase.auth.signInWithCredential(authCredential).await().user
                    user?.let {onLoginSuccess(it)}
                }
            } catch (e: NoCredentialException) {
                // No saved Google account credentials; prompt user to add account (optional)
                launcher?.launch(addGoogleAccountIntent())
            } catch (t: Throwable) {
                onError(t)
            }
        }
    }

    private fun buildGoogleIdRequest(context: Context): GetCredentialRequest {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)     // show all Google accounts
            .setAutoSelectEnabled(false)
            .setServerClientId(context.getString(R.string.web_client_id)) // from Firebase Console
            .build()

        return GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
    }

    /** Optional: opens Settings to add a Google account on the device */
    fun addGoogleAccountIntent(): Intent {
        return Intent(Settings.ACTION_ADD_ACCOUNT).apply {
            putExtra(Settings.EXTRA_ACCOUNT_TYPES, arrayOf("com.google"))
        }
    }
}





