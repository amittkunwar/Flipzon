package com.flipzon.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flipzon.app.datastore.SessionManager
import com.flipzon.app.repository.AuthRepository
import com.flipzon.app.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val cartRepository: CartRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    var loginState by mutableStateOf<LoginState>(LoginState.Idle)
        private set

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            loginState = LoginState.Loading
            try {
                val res = authRepository.login(email, pass)
                sessionManager.saveSession(res.id, res.email, res.firstName, res.lastName, res.image)
                loginState = LoginState.Success
            } catch (e: Exception) {
                loginState = LoginState.Error(e.message ?: "Login failed")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            sessionManager.clearSession()
            cartRepository.clearCart() 
            loginState = LoginState.Idle
        }
    }

    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        object Success : LoginState()
        data class Error(val message: String) : LoginState()
    }
}
