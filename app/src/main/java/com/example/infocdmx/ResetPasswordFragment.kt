package com.example.infocdmx

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.infocdmx.databinding.FragmentResetPasswordBinding

class ResetPasswordFragment : Fragment() {

    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        setupValidation()

        binding.buttonReset.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            Toast.makeText(requireContext(), "Instrucciones enviadas a $email", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupValidation() {
        binding.buttonReset.isEnabled = false

        binding.editTextEmail.addTextChangedListener {
            val email = it.toString().trim()
            val isValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
            
            binding.tilEmail.error = if (email.isEmpty() || isValid) null else "Correo inválido"
            binding.buttonReset.isEnabled = isValid
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
