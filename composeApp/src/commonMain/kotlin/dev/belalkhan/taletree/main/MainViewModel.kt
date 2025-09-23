package dev.belalkhan.taletree.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.belalkhan.taletree.firebase.auth.FirebaseAuthClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MainState(
    val isLogoutDialogVisible: Boolean = false
)


class MainViewModel(
    private val firebaseAuth: FirebaseAuthClient
) : ViewModel() {

    private val _state: MutableStateFlow<MainState> = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()


    fun onLogoutRequested() {
        _state.update { it.copy(isLogoutDialogVisible = true) }
    }

    fun dismissLogoutDialog() {
        _state.update { it.copy(isLogoutDialogVisible = false) }
    }

    fun onLogout() = viewModelScope.launch {
        _state.update { it.copy(isLogoutDialogVisible = false) }
        firebaseAuth.signOut()
    }
}