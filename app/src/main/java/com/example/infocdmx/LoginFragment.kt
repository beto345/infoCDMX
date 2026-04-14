package com.example.infocdmx

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.example.infocdmx.databinding.FragmentLoginBinding
import androidx.navigation.fragment.findNavController

class LoginFragment : Fragment() {
    private var _binding : FragmentLoginBinding? = null
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

        // Navegar al registro al hacer clic en el texto de registro
        binding.textViewRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        
        setupValidation()

        binding.buttonLogin.setOnClickListener {
            // Lógica de inicio de sesión exitosa
        }
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

        val isEmailValid = isValidEmail(email)
        val isPasswordValid = password.length >= 8

        binding.tilEmail.error = if (email.isEmpty() || isEmailValid) null else "Correo inválido"
        binding.tilPassword.error = if (password.isEmpty() || isPasswordValid) null else "Mínimo 8 caracteres"

        binding.buttonLogin.isEnabled = email.isNotEmpty() && password.isNotEmpty() && isEmailValid && isPasswordValid
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
