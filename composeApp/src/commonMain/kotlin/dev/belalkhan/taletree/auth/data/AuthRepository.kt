package dev.belalkhan.taletree.auth.data

import dev.belalkhan.taletree.firebase.auth.FirebaseAuthClient
import dev.belalkhan.taletree.firebase.auth.KmpFirebaseUser

interface AuthRepository {
    suspend fun login(email: String, password: String): AuthResult

    sealed class AuthResult {
        data class Success(val user: KmpFirebaseUser) : AuthResult()
        data class Error(val message: String) : AuthResult()
    }
}

class AuthRepositoryImpl(
    private val firebaseAuthClient: FirebaseAuthClient
) : AuthRepository {
    override suspend fun login(
        email: String, password: String
    ): AuthRepository.AuthResult {
        return try {
            val user = firebaseAuthClient.signIn(email, password)
            AuthRepository.AuthResult.Success(user)
        } catch (e: Exception) {
            AuthRepository.AuthResult.Error(e.message ?: "Unknown error")
        }
    }
}
