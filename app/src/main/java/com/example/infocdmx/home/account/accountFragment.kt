package com.example.infocdmx.home.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import android.util.Log
import com.example.infocdmx.core.ResponseService
import com.example.infocdmx.databinding.FragmentCuentaBinding
import com.example.infocdmx.onboarding.MainActivity
import com.example.infocdmx.onboarding.personal.model.UserProfile
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class AccountFragment : Fragment() {

    private var _binding: FragmentCuentaBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<AccountViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCuentaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        observeViewModel()
        
        // Mostrar email desde Auth mientras carga el resto
        val authUser = FirebaseAuth.getInstance().currentUser
        if (authUser != null) {
            binding.tvUserEmail.text = authUser.email
            fetchData()
        } else {
            // Si no hay sesión, volver al login
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userState.collect { state ->
                    when (state) {
                        is ResponseService.Loading -> {
                            // Mostrar cargando si es necesario
                        }
                        is ResponseService.Success -> {
                            updateUI(state.data)
                        }
                        is ResponseService.Error -> {
                            Snackbar.make(binding.root, state.error, Snackbar.LENGTH_LONG).show()
                        }
                        null -> Unit
                    }
                }
            }
        }
    }

    private fun fetchData() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            viewModel.fetchUserInfo(user.uid)
        }
    }

    private fun updateUI(user: UserProfile) {
        binding.tvUserName.text = "@${user.userName}"
        binding.tvDisplayFullName.text = "${user.firstName} ${user.lastName}"
        binding.tvDisplayPhone.text = user.phone
        
        // Inicial del avatar
        val initial = if (user.firstName.isNotEmpty()) user.firstName.first().toString() else "U"
        binding.tvAvatarInitial.text = initial.uppercase()
    }

    private fun setupClickListeners() {
        binding.btnCerrarSesion.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}