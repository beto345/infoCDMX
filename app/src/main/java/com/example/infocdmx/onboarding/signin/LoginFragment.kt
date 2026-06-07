package com.example.infocdmx.onboarding.signin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.infocdmx.R
import com.example.infocdmx.onboarding.signin.SignViewModel
import com.example.infocdmx.core.FragmentCommunicator
import com.example.infocdmx.core.ResponseService
import com.example.infocdmx.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.example.infocdmx.home.HomeActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<SignViewModel>()
    private lateinit var communicator: FragmentCommunicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        communicator = requireActivity() as FragmentCommunicator
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkSession()
        setupValidation()
        setupClickListeners()
        observeState()
    }

    private fun checkSession() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            navigateToHome()
        }
    }

    private fun navigateToHome() {
        val intent = Intent(requireContext(), HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun setupValidation() {
        binding.buttonLogin.isEnabled = false
        binding.editTextEmail.addTextChangedListener { validateAndEnable() }
        binding.editTextPassword.addTextChangedListener { validateAndEnable() }
    }

    private fun validateAndEnable() {
        val email = binding.editTextEmail.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()

        binding.tilEmail.error = viewModel.validateEmail(email)
        binding.tilPassword.error = viewModel.validatePassword(password)
        binding.buttonLogin.isEnabled = viewModel.isLoginFormValid(email, password)
    }

    private fun setupClickListeners() {
        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            viewModel.requestLogin(email, password)
        }
        binding.textViewRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.textViewForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_resetPasswordFragment)
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.signInState.collect { state ->
                    when (state) {
                        is ResponseService.Loading -> {
                            communicator.manageLoader(true)
                            binding.buttonLogin.isEnabled = false
                        }
                        is ResponseService.Success -> {
                            communicator.manageLoader(false)
                            navigateToHome()
                        }
                        is ResponseService.Error -> {
                            communicator.manageLoader(false)
                            binding.buttonLogin.isEnabled = true
                            Snackbar.make(binding.root, state.error, Snackbar.LENGTH_LONG).show()
                        }
                        null -> Unit
                    }
                }
            }
        }
    }
}