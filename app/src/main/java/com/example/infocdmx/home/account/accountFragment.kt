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
import com.example.infocdmx.core.ResponseService
import com.example.infocdmx.databinding.FragmentCuentaBinding
import com.example.infocdmx.onboarding.MainActivity
import com.example.infocdmx.onboarding.personal.model.UserProfile
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class accountFragment : Fragment() {

    private var _binding: FragmentCuentaBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<AccountViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCuentaBinding.inflate(inflater, container, false)
        setupClickListeners()
        observeViewModel()
        fetchData()
        return binding.root
    }

    private fun fetchData() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            viewModel.fetchUserInfo(uid)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userState.collect { state ->
                    when (state) {
                        is ResponseService.Loading -> {
                            // Podrías mostrar un mini loader si quieres
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

    private fun updateUI(user: UserProfile) {
        val authUser = FirebaseAuth.getInstance().currentUser
        
        binding.tvUserName.text = "${user.firstName} ${user.lastName}"
        binding.tvUserEmail.text = authUser?.email ?: ""
        binding.tvDisplayFullName.text = "${user.firstName} ${user.lastName}"
        binding.tvDisplayPhone.text = user.phone
        
        // Inicial del avatar
        val initial = user.firstName.firstOrNull()?.toString() ?: "U"
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