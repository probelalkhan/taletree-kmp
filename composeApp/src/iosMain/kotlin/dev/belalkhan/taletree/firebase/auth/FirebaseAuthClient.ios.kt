package dev.belalkhan.taletree.firebase.auth

import cocoapods.FirebaseAuth.FIRAuth
import cocoapods.FirebaseAuth.FIRUser
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSError
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class FirebaseAuthClient {

    @OptIn(ExperimentalForeignApi::class)
    private val auth: FIRAuth by lazy { FIRAuth.auth() }

    @OptIn(ExperimentalForeignApi::class)
    actual val authState: StateFlow<AuthState> by lazy {
        val flow = callbackFlow {
            val listener = auth.addAuthStateDidChangeListener { _, user ->
                val state = if (user == null) AuthState.Unauthenticated else AuthState.Authenticated
                trySend(state).isSuccess
            }

            awaitClose { auth.removeAuthStateDidChangeListener(listener) }
        }
        flow.stateIn(
            scope = CoroutineScope(Dispatchers.Main),
            started = SharingStarted.Eagerly,
            initialValue = if (auth.currentUser() == null) AuthState.Unauthenticated else AuthState.Authenticated
        )
    }

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun signIn(email: String, password: String): KmpFirebaseUser =
        suspendCancellableCoroutine { cont ->
            auth.signInWithEmail(email, password = password) { authResult, error: NSError? ->
                when {
                    error != null -> {
                        cont.resumeWithException(
                            Exception(error.localizedDescription)
                        )
                    }

                    authResult?.user() != null -> {
                        val u = authResult.user()
                        cont.resume(
                            KmpFirebaseUser(
                                uid = u.uid(),
                                email = u.email(),
                                displayName = u.displayName()
                            )
                        )
                    }

                    else -> {
                        cont.resumeWithException(Exception("Auth succeeded but user is null"))
                    }
                }
            }
            cont.invokeOnCancellation { /* no-op */ }
        }

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun signOut() {
        // Objective-C version takes an NSErrorPointer (nullable) – pass null
        auth.signOut(null)
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun getCurrentUser(): KmpFirebaseUser? {
        val u: FIRUser? = auth.currentUser()
        return u?.let {
            KmpFirebaseUser(
                uid = it.uid(),
                email = it.email(),
                displayName = it.displayName()
            )
        }
    }
}
