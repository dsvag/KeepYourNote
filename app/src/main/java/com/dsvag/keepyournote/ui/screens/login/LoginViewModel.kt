package com.dsvag.keepyournote.ui.screens.login

import android.util.Patterns
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginViewModel @ViewModelInject constructor() : ViewModel() {
    private val _mutableState = MutableLiveData<LoginState>(LoginState.Default)
    val state get() = _mutableState

    private lateinit var auth: FirebaseAuth

    fun initAuth(auth: FirebaseAuth) {
        this.auth = auth
    }

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    fun auth(email: String?, password: String?) {
        if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
            _mutableState.value = LoginState.Loading

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _mutableState.value = LoginState.Success
                } else {
                    _mutableState.value = LoginState.Error
                }
            }
        } else {
            _mutableState.value = LoginState.Error
        }
    }

    fun checkEmail(email: String?): Boolean {
        val isCorrect = !email.isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()

        when {
            isCorrect -> _mutableState.value = LoginState.Default
            email.isNullOrEmpty() -> _mutableState.value = LoginState.Default
            else -> _mutableState.value = LoginState.LoginError
        }

        return isCorrect
    }

    sealed class LoginState {
        object Default : LoginState()
        object Loading : LoginState()
        object Success : LoginState()
        object LoginError : LoginState()
        object Error : LoginState()
    }
}