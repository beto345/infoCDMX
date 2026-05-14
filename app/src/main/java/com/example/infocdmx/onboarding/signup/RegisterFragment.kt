package com.example.infocdmx.onboarding.signup

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
import com.example.infocdmx.core.FragmentCommunicator
import com.example.infocdmx.core.ResponseService
import com.example.infocdmx.databinding.FragmentRegisterBinding
import com.example.infocdmx.onboarding.signin.SignViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<SignViewModel>()
    private lateinit var communicator: FragmentCommunicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        communicator = requireActivity() as FragmentCommunicator
        setupValidation()
        setupClickListeners()
        observeState()
        return binding.root
    }

    private fun setupValidation() {
        binding.buttonRegister.isEnabled = false
        binding.editTextNombre.addTextChangedListener { validateAndEnable() }
        binding.editTextEmail.addTextChangedListener { validateAndEnable() }
        binding.editTextPassword.addTextChangedListener { validateAndEnable() }
    }

    private fun validateAndEnable() {
        val nombre = binding.editTextNombre.text.toString().trim()
        val email = binding.editTextEmail.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()

        binding.tilNombre.error = if (nombre.isBlank()) "El nombre es requerido" else null
        binding.tilEmail.error = viewModel.validateEmail(email)
        binding.tilPassword.error = viewModel.validatePassword(password)

        binding.buttonRegister.isEnabled =
            nombre.isNotBlank() &&
                    viewModel.validateEmail(email) == null &&
                    viewModel.validatePassword(password) == null
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.buttonRegister.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            viewModel.requestSignUp(email, password)
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registerState.collect { state ->
                    when (state) {
                        is ResponseService.Loading -> {
                            communicator.manageLoader(true)
                            binding.buttonRegister.isEnabled = false
                        }
                        is ResponseService.Success -> {
                            communicator.manageLoader(false)
                            Snackbar.make(
                                binding.root,
                                "Cuenta creada exitosamente.",
                                Snackbar.LENGTH_LONG
                            ).show()
                            findNavController().navigate(R.id.action_registerFragment_to_personalInfoFragment)
                        }
                        is ResponseService.Error -> {
                            communicator.manageLoader(false)
                            binding.buttonRegister.isEnabled = true
                            Snackbar.make(binding.root, state.error, Snackbar.LENGTH_LONG).show()
                        }
                        null -> Unit
                    }
                }
            }
        }
    }
}