package com.dsvag.keepyournote.ui.screens.login

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

        binding.loginButton.setOnClickListener {
            if (loginViewModel.checkEmail(binding.login.editText?.text.toString())) {
                loginViewModel.auth(
                    binding.login.editText?.text.toString(),
                    binding.password.editText?.text.toString()
                )
            }
        }

        binding.login.editText?.addTextChangedListener { text: Editable? ->
            loginViewModel.checkEmail(text.toString())
        }
    }

    override fun onStart() {
        super.onStart()

        activity?.onBackPressedDispatcher?.addCallback { activity?.finish() }

        loginViewModel.initAuth(Firebase.auth)

        if (loginViewModel.getCurrentUser() != null) {
            findNavController().navigate(R.id.action_loginFragment_to_noteListFragment)
        }

        binding.login.requestFocus()
    }

    private fun stateObserver(state: LoginViewModel.LoginState) {
        when (state) {
            LoginViewModel.LoginState.Default -> onDefault()
            LoginViewModel.LoginState.Loading -> onLoading()
            LoginViewModel.LoginState.Success -> onSuccess()
            LoginViewModel.LoginState.LoginError -> onLoginError()
            LoginViewModel.LoginState.Error -> onError()
        }
    }

    private fun onDefault() {
        binding.login.isErrorEnabled = false
        binding.loadingIndicator.isVisible = false
    }

    private fun onLoading() {
        binding.login.isErrorEnabled = false
        binding.loadingIndicator.isVisible = true
    }

    private fun onSuccess() {
        findNavController().navigate(R.id.action_loginFragment_to_noteListFragment)
        binding.loadingIndicator.isVisible = false
    }

    private fun onError() {
        Toast.makeText(requireContext(), "Authentication failed.", Toast.LENGTH_SHORT).show()
        binding.loadingIndicator.isVisible = false
    }

    private fun onLoginError() {
        binding.login.isErrorEnabled = true
        binding.login.error = "Invalid email"
        binding.loadingIndicator.isVisible = false
    }
}