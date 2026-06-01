package com.example.practice.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        _authState.value =
            if (auth.currentUser == null) AuthState.Unauthenticated
            else AuthState.Authenticated
    }

    fun login(email: String, password: String) {

        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Fields cannot be empty")
            return
        }

        _authState.value = AuthState.Loading

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error("Login failed")
                }
            }
    }

    fun signup(email: String, password: String) {

        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Fields cannot be empty")
            return
        }

        _authState.value = AuthState.Loading

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    auth.signOut()
                    _authState.value = AuthState.Error("Signup successful")
                } else {
                    _authState.value = AuthState.Error("Signup failed")
                }
            }
    }

    fun signout() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }
}