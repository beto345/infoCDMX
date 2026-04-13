package com.example.infocdmx

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.infocdmx.databinding.FragmentLoginBinding
import androidx.navigation.fragment.findNavController
import androidx.core.widget.addTextChangedListener

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupValidation()
    }

    private fun setupValidation() {
        binding.buttonLogin.isEnabled = false

        binding.editTextEmail.addTextChangedListener {
            validateFields()
        }
        binding.editTextPassword.addTextChangedListener {
            validateFields()
        }
    }

    private fun validateFields() {
        val email = binding.editTextEmail.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()

        val isEmailValid = isEmailValid(email)
        val isPasswordValid = password.length >= 8

        binding.editTextEmail.error =
            if (email.isEmpty() || isEmailValid) null else "Correo Invalido"

        binding.editTextPassword.error =
            if (password.isEmpty() || isPasswordValid) null else "Minimo 8 caracteres"

        binding.buttonLogin.isEnabled =
            email.isNotEmpty() && password.isNotEmpty() && isEmailValid && isPasswordValid
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}