package com.example.infocdmx

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.example.infocdmx.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        setupValidation()

        binding.buttonRegister.setOnClickListener {
            // Acción de registro exitoso
        }
    }

    private fun setupValidation() {
        binding.buttonRegister.isEnabled = false

        binding.editTextNombre.addTextChangedListener { validateFields() }
        binding.editTextEmail.addTextChangedListener { validateFields() }
        binding.editTextPassword.addTextChangedListener { validateFields() }
    }

    private fun validateFields() {
        val nombre = binding.editTextNombre.text.toString().trim()
        val email = binding.editTextEmail.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()

        val isNombreValid = nombre.isNotEmpty()
        val isEmailValid = isValidEmail(email)
        val isPasswordValid = password.length >= 8

        // Mostrar errores solo si el campo no está vacío pero es inválido
        binding.tilNombre.error = if (nombre.isNotEmpty() || isNombreValid) null else "Nombre obligatorio"
        binding.tilEmail.error = if (email.isEmpty() || isEmailValid) null else "Correo inválido"
        binding.tilPassword.error = if (password.isEmpty() || isPasswordValid) null else "Mínimo 8 caracteres"

        binding.buttonRegister.isEnabled = isNombreValid && isEmailValid && isPasswordValid
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
