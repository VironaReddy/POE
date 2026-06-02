package com.example.practice.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practice.data.UserRepository
import com.example.practice.model.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val userRepository = UserRepository()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> = _currentUser

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        val firebaseUser = auth.currentUser
        if (firebaseUser == null) {
            _authState.value = AuthState.Unauthenticated
            _currentUser.value = null
        } else {
            _authState.value = AuthState.Authenticated
            loadUserProfile(firebaseUser.uid)
        }
    }

    private fun loadUserProfile(userId: String) {
        viewModelScope.launch {
            try {
                val user = userRepository.getUser(userId)
                _currentUser.value = user
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Fields cannot be empty")
            return
        }

        _authState.value = AuthState.Loading

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = task.result?.user?.uid
                    if (userId != null) loadUserProfile(userId)
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Login failed")
                }
            }
    }

    fun signup(email: String, username: String, password: String) {
        if (email.isBlank() || password.isBlank() || username.isBlank()) {
            _authState.value = AuthState.Error("Fields cannot be empty")
            return
        }

        _authState.value = AuthState.Loading

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = task.result?.user
                    if (firebaseUser != null) {
                        val newUser = User(
                            id = firebaseUser.uid,
                            username = username,
                            email = email,
                            phone = ""
                        )
                        viewModelScope.launch {
                            try {
                                userRepository.saveUser(newUser)
                                _currentUser.value = newUser
                                _authState.value = AuthState.Authenticated
                            } catch (e: Exception) {
                                _authState.value = AuthState.Error("Failed to save user profile")
                            }
                        }
                    }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Signup failed")
                }
            }
    }

    fun updateUserProfile(user: User) {
        viewModelScope.launch {
            try {
                userRepository.saveUser(user)
                _currentUser.value = user
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun signout() {
        auth.signOut()
        _currentUser.value = null
        _authState.value = AuthState.Unauthenticated
    }
}