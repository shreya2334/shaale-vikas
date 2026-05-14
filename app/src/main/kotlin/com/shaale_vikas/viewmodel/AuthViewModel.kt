package com.shaale_vikas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaale_vikas.model.User
import com.shaale_vikas.repository.AuthRepository
import com.shaale_vikas.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repo = AuthRepository()

    private val _state =
        MutableStateFlow<Resource<User>?>(null)

    val state = _state.asStateFlow()

    val isLoggedIn
        get() = repo.isLoggedIn

    val currentUserId
        get() = repo.currentUser?.uid

    fun login(
        email: String,
        password: String
    ) = viewModelScope.launch {

        _state.value = Resource.Loading

        _state.value =
            repo.login(email, password)
    }

    fun register(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
        role: String,
        school: String,
        year: String
    ) {

        if (password != confirmPassword) {

            _state.value =
                Resource.Error(
                    "Passwords don't match"
                )

            return
        }

        if (
            name.isBlank() ||
            email.isBlank() ||
            password.length < 6
        ) {

            _state.value =
                Resource.Error(
                    "Please fill all fields correctly"
                )

            return
        }

        viewModelScope.launch {

            _state.value = Resource.Loading

            _state.value =
                repo.register(
                    name,
                    email,
                    password,
                    role,
                    school,
                    year
                )
        }
    }

    fun loadCurrentUser() =
        viewModelScope.launch {

            val uid =
                repo.currentUser?.uid
                    ?: return@launch

            _state.value =
                repo.getUserProfile(uid)
        }

    fun logout() {
        repo.logout()
    }

    fun reset() {
        _state.value = null
    }
}