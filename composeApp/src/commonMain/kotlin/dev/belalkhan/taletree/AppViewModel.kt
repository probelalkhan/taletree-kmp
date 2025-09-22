package dev.belalkhan.taletree

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.belalkhan.taletree.firebase.auth.AuthState
import dev.belalkhan.taletree.firebase.auth.FirebaseAuthClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AppViewModel(
    private val firebaseAuthClient: FirebaseAuthClient
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState

    init {
        checkUserSession()
    }

    private fun checkUserSession() {
        viewModelScope.launch {
            firebaseAuthClient.authState.collect { state ->
                _authState.value = state
            }
        }
    }
}