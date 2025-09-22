package dev.belalkhan.taletree.firebase.auth

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class FirebaseAuthClient {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }


    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    actual val authState: StateFlow<AuthState> = _authState

    init {
        auth.addAuthStateListener { auth ->
            if (auth.currentUser == null) {
                _authState.value = AuthState.Unauthenticated
            } else {
                _authState.value = AuthState.Authenticated
            }
        }
    }

    actual suspend fun signIn(email: String, password: String): KmpFirebaseUser =
        suspendCancellableCoroutine { cont ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val u = auth.currentUser
                        if (u != null) cont.resume(
                            KmpFirebaseUser(
                                uid = u.uid,
                                email = u.email,
                                displayName = u.displayName
                            )
                        )
                        else cont.resumeWithException(Exception("Auth succeeded but user is null"))
                    } else {
                        cont.resumeWithException(task.exception ?: Exception("Unknown auth error"))
                    }
                }
            cont.invokeOnCancellation { /* no-op */ }
        }

    actual suspend fun signOut() {
        auth.signOut()
    }

    actual fun getCurrentUser(): KmpFirebaseUser? {
        val u = auth.currentUser
        return u?.let {
            KmpFirebaseUser(
                uid = it.uid,
                email = it.email,
                displayName = it.displayName
            )
        }
    }
}