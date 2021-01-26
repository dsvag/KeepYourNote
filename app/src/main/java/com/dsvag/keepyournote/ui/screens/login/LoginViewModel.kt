package com.dsvag.keepyournote.ui.screens.login

import android.util.Patterns
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginViewModel @ViewModelInject constructor() : ViewModel() {
    private val _mutableState = MutableLiveData<State>(State.Login)
    val state get() = _mutableState

    private val _mutableEmailState = MutableLiveData<InputState>(InputState.Default)
    val emailState get() = _mutableEmailState

    private val _mutablePasswordState = MutableLiveData<InputState>(InputState.Default)
    val passwordState get() = _mutablePasswordState

    private lateinit var auth: FirebaseAuth

    fun initAuth(auth: FirebaseAuth) {
        this.auth = auth
    }

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    fun login() {
        _mutableState.value = State.Login
    }

    fun login(email: String?, password: String?) {
        if (emailState.value == InputState.Success && passwordState.value == InputState.Success) {
            _mutableState.value = State.Loading

            auth.signInWithEmailAndPassword(email!!, password!!).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _mutableState.value = State.Success
                } else {
                    _mutableState.value = State.Error(task.exception?.message.toString())
                }
            }
        } else {
            _mutableState.value = State.Error("Authentication failed")
        }
    }

    fun registration() {
        _mutableState.value = State.Registration
    }

    fun registration(email: String?, password: String?) {
        if (emailState.value == InputState.Success && passwordState.value == InputState.Success) {
            _mutableState.value = State.Loading

            auth.createUserWithEmailAndPassword(email!!, password!!).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _mutableState.value = State.Success
                } else {
                    _mutableState.value = State.Error(task.exception?.message.toString())
                }
            }
        } else {
            _mutableState.value = State.Error("Registration failed")
        }
    }

    fun checkEmail(email: String?): Boolean {
        val isCorrect =
            !email.isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()

        when {
            isCorrect -> _mutableEmailState.value = InputState.Success
            email.isNullOrEmpty() -> _mutableEmailState.value = InputState.Default
            else -> _mutableEmailState.value = InputState.Error
        }

        return isCorrect
    }

    fun checkPassword(password: String?): Boolean {
        val regex = Regex("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*+=]).{6,32}\$")

        val isCorrect = !password.isNullOrEmpty() && regex.containsMatchIn(password)

        when {
            isCorrect -> _mutablePasswordState.value = InputState.Success
            password.isNullOrEmpty() -> _mutablePasswordState.value = InputState.Default
            else -> _mutablePasswordState.value = InputState.Error
        }

        return isCorrect
    }

    sealed class State {
        object Login : State()
        object Registration : State()

        object Loading : State()

        object Success : State()
        class Error(val msg: String) : State()
    }

    sealed class InputState {
        object Success : InputState()
        object Error : InputState()
        object Default : InputState()
    }
}