package com.dsvag.keepyournote.ui.login

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dsvag.keepyournote.R
import com.dsvag.keepyournote.databinding.FragmentLoginBinding
import com.dsvag.keepyournote.ui.viewBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val binding by viewBinding(FragmentLoginBinding::bind)

    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity?)?.supportActionBar?.hide()

        loginViewModel.state.observe(this.viewLifecycleOwner, this::stateObserver)
        loginViewModel.emailState.observe(this.viewLifecycleOwner, this::emailObserver)
        loginViewModel.passwordState.observe(this.viewLifecycleOwner, this::passwordObserver)

        binding.login.editText?.addTextChangedListener { text: Editable? ->
            loginViewModel.checkEmail(text.toString())
            loginViewModel.checkPassword(binding.password.editText?.text.toString())
        }

        binding.password.editText?.addTextChangedListener { text: Editable? ->
            loginViewModel.checkPassword(text.toString())
            loginViewModel.checkEmail(binding.login.editText?.text.toString())
        }

        binding.createAccount.setOnClickListener {
            loginViewModel.registration()
        }
    }

    override fun onStart() {
        super.onStart()

        activity?.onBackPressedDispatcher?.addCallback { loginViewModel.login() }

        loginViewModel.initAuth(Firebase.auth)

        if (loginViewModel.getCurrentUser != null) {
            findNavController().navigate(R.id.action_loginFragment_to_noteListFragment)
        }

        binding.login.requestFocus()
    }

    private fun stateObserver(state: LoginViewModel.State) {
        when (state) {
            LoginViewModel.State.Login -> onLogin()
            LoginViewModel.State.Registration -> onRegistration()

            LoginViewModel.State.Loading -> onLoading()

            LoginViewModel.State.Success -> onSuccess()
            is LoginViewModel.State.Error -> onError(state.msg)
        }
    }

    private fun emailObserver(state: LoginViewModel.InputState) {
        when (state) {
            LoginViewModel.InputState.Success -> onLoginSuccess()
            LoginViewModel.InputState.Error -> onLoginError()
            LoginViewModel.InputState.Default -> onLoginEmpty()
        }
    }

    private fun passwordObserver(state: LoginViewModel.InputState) {
        when (state) {
            LoginViewModel.InputState.Success -> onPasswordSuccess()
            LoginViewModel.InputState.Error -> onPasswordError()
            LoginViewModel.InputState.Default -> onPasswordEmpty()
        }
    }

    private fun onLogin() {
        binding.createAccount.isVisible = true
        binding.loadingIndicator.isVisible = false

        binding.loginButton.setOnClickListener {
            if (loginViewModel.checkEmail(binding.login.editText?.text.toString())) {
                loginViewModel.login(
                    binding.login.editText?.text.toString(),
                    binding.password.editText?.text.toString()
                )
            }
        }

        binding.loginButton.text = "Login"
    }

    private fun onRegistration() {
        binding.createAccount.isVisible = false
        binding.loadingIndicator.isVisible = false

        binding.loginButton.setOnClickListener {
            if (loginViewModel.checkEmail(binding.login.editText?.text.toString())) {
                loginViewModel.registration(
                    binding.login.editText?.text.toString(),
                    binding.password.editText?.text.toString()
                )
            }
        }

        binding.loginButton.text = "Create account"
    }

    private fun onLoading() {
        binding.loadingIndicator.isVisible = true
    }

    private fun onSuccess() {
        findNavController().navigate(R.id.action_loginFragment_to_noteListFragment)
        binding.loadingIndicator.isVisible = false
    }

    private fun onError(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        binding.loadingIndicator.isVisible = false
    }

    private fun onLoginSuccess() {
        binding.login.isErrorEnabled = false
        binding.login.isHelperTextEnabled = true
        binding.login.helperText = "Login correct"

        binding.loginButton.isEnabled = true
    }

    private fun onLoginError() {
        binding.login.isErrorEnabled = true
        binding.login.isHelperTextEnabled = false
        binding.login.error = "Invalid email"

        binding.loginButton.isEnabled = false
    }

    private fun onLoginEmpty() {
        binding.login.isErrorEnabled = false
        binding.login.isHelperTextEnabled = false

        binding.loginButton.isEnabled = false
    }

    private fun onPasswordSuccess() {
        binding.password.isErrorEnabled = false
        binding.password.isCounterEnabled = true
        binding.password.isHelperTextEnabled = true
        binding.password.helperText = "Password correct"

        binding.loginButton.isEnabled = true
    }

    private fun onPasswordError() {
        binding.password.isErrorEnabled = true
        binding.password.isCounterEnabled = true
        binding.password.isHelperTextEnabled = false
        binding.password.error = "6 to 32 characters, one number,\n" +
                "at least one uppercase and one lowercase English letter,\n" +
                "one special character #?!@$%^&*+="

        binding.loginButton.isEnabled = false
    }

    private fun onPasswordEmpty() {
        binding.password.isErrorEnabled = false
        binding.password.isCounterEnabled = false
        binding.password.isHelperTextEnabled = false

        binding.loginButton.isEnabled = false
    }
}