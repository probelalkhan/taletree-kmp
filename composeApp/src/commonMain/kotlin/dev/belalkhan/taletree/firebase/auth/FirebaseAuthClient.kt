package dev.belalkhan.taletree.firebase.auth

import kotlinx.coroutines.flow.StateFlow

data class KmpFirebaseUser(
    val uid: String,
    val email: String?,
    val displayName: String?
)

sealed class AuthState {
    object Loading : AuthState()
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
}

expect class FirebaseAuthClient() {
    suspend fun signIn(
        email: String,
        password: String
    ): KmpFirebaseUser

    suspend fun signOut()

    fun getCurrentUser(): KmpFirebaseUser?

    val authState: StateFlow<AuthState>
}