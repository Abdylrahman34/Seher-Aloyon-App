package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.FirebaseManager
import com.example.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Authenticated(val profile: UserProfile) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

class AuthViewModel(private val firebaseManager: FirebaseManager) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _displayName = MutableStateFlow("")
    val displayName: StateFlow<String> = _displayName.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()

    private val _isLoginMode = MutableStateFlow(true)
    val isLoginMode: StateFlow<Boolean> = _isLoginMode.asStateFlow()

    private val _isPasswordVisible = MutableStateFlow(false)
    val isPasswordVisible: StateFlow<Boolean> = _isPasswordVisible.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        checkExistingSession()
    }

    fun checkExistingSession() {
        val session = firebaseManager.getCurrentSession()
        if (session != null) {
            _uiState.value = AuthUiState.Authenticated(session)
        }
    }

    fun onUsernameChanged(input: String) {
        _username.value = input
        _errorMessage.value = null
    }

    fun onPasswordChanged(input: String) {
        _password.value = input
        _errorMessage.value = null
    }

    fun onDisplayNameChanged(input: String) {
        _displayName.value = input
        _errorMessage.value = null
    }

    fun onConfirmPasswordChanged(input: String) {
        _confirmPassword.value = input
        _errorMessage.value = null
    }

    fun toggleLoginMode() {
        _isLoginMode.value = !_isLoginMode.value
        _errorMessage.value = null
    }

    fun togglePasswordVisibility() {
        _isPasswordVisible.value = !_isPasswordVisible.value
    }

    fun fillDemoCredentials() {
        _username.value = "aster"
        _password.value = "luxe123"
        _errorMessage.value = null
    }

    fun submitAuth() {
        val user = _username.value.trim()
        val pass = _password.value

        // Front-end validations
        val userErr = firebaseManager.validateUsername(user)
        if (userErr != null) {
            _errorMessage.value = userErr
            return
        }

        val passErr = firebaseManager.validatePassword(pass)
        if (passErr != null) {
            _errorMessage.value = passErr
            return
        }

        if (!_isLoginMode.value) {
            if (pass != _confirmPassword.value) {
                _errorMessage.value = "Passwords do not match"
                return
            }
        }

        _uiState.value = AuthUiState.Loading
        _errorMessage.value = null

        viewModelScope.launch {
            if (_isLoginMode.value) {
                val result = firebaseManager.loginUser(user, pass)
                result.fold(
                    onSuccess = { profile ->
                        _uiState.value = AuthUiState.Authenticated(profile)
                    },
                    onFailure = { error ->
                        _uiState.value = AuthUiState.Idle
                        _errorMessage.value = error.message ?: "Authentication failed. Please verify credentials."
                    }
                )
            } else {
                val result = firebaseManager.registerUser(
                    username = user,
                    password = pass,
                    displayName = _displayName.value.trim()
                )
                result.fold(
                    onSuccess = { profile ->
                        _uiState.value = AuthUiState.Authenticated(profile)
                    },
                    onFailure = { error ->
                        _uiState.value = AuthUiState.Idle
                        _errorMessage.value = error.message ?: "Registration failed. Username may already exist."
                    }
                )
            }
        }
    }

    fun logout() {
        firebaseManager.logout()
        _uiState.value = AuthUiState.Idle
        _username.value = ""
        _password.value = ""
        _displayName.value = ""
        _confirmPassword.value = ""
        _errorMessage.value = null
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
