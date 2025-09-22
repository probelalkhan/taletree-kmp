package dev.belalkhan.taletree.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.belalkhan.taletree.firebase.auth.FirebaseAuthClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeState(
    val isLogoutDialogVisible: Boolean = false
)


class HomeViewModel(
    private val firebaseAuth: FirebaseAuthClient
) : ViewModel() {

    private val _state: MutableStateFlow<HomeState> = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()


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