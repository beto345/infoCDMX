package com.example.infocdmx.onboarding.personal.model

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.infocdmx.core.FragmentCommunicator
import com.example.infocdmx.core.ResponseService
import com.example.infocdmx.databinding.FragmentPersonalInfoBinding
import com.example.infocdmx.home.HomeActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.util.Calendar

class PersonalInfoFragment : Fragment() {

    private var _binding: FragmentPersonalInfoBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<PersonalInfoViewModel>()
    private lateinit var communicator: FragmentCommunicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalInfoBinding.inflate(inflater, container, false)
        communicator = requireActivity() as FragmentCommunicator
        setupValidation()
        setupDatePicker()
        setupClickListeners()
        observeState()
        return binding.root
    }

    private fun setupValidation() {
        binding.btnContinuar.isEnabled = false
        binding.firstNameTiet.addTextChangedListener { validateAndEnable() }
        binding.lastNameTiet.addTextChangedListener { validateAndEnable() }
        binding.usernameTiet.addTextChangedListener { validateAndEnable() }
        binding.phoneTiet.addTextChangedListener { validateAndEnable() }
        binding.birthDateTiet.addTextChangedListener { validateAndEnable() }
    }

    private fun validateAndEnable() {
        val firstName = binding.firstNameTiet.text.toString().trim()
        val lastName = binding.lastNameTiet.text.toString().trim()
        val username = binding.usernameTiet.text.toString().trim()
        val phone = binding.phoneTiet.text.toString().trim()
        val birthDate = binding.birthDateTiet.text.toString().trim()

        binding.firstNameTil.error = viewModel.validateFirstName(firstName)
        binding.lastNameTil.error = viewModel.validateLastName(lastName)
        binding.usernameTil.error = viewModel.validateUsername(username)
        binding.phoneTil.error = viewModel.validatePhone(phone)
        binding.birthDateTil.error = viewModel.validateBirthDate(birthDate)

        binding.btnContinuar.isEnabled =
            viewModel.isFormValid(firstName, lastName, username, phone, birthDate)
    }

    private fun setupDatePicker() {
        binding.birthDateTiet.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    val formatted = "%02d / %02d / %04d".format(day, month + 1, year)
                    binding.birthDateTiet.setText(formatted)
                    validateAndEnable()
                },
                cal.get(Calendar.YEAR) - 18,
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).apply {
                datePicker.maxDate = System.currentTimeMillis()
            }.show()
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnContinuar.setOnClickListener {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            if (uid == null) {
                Snackbar.make(binding.root, "Sesión inválida", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }
            viewModel.saveProfile(
                uid = uid,
                firstName = binding.firstNameTiet.text.toString().trim(),
                lastName = binding.lastNameTiet.text.toString().trim(),
                username = binding.usernameTiet.text.toString().trim(),
                phone = binding.phoneTiet.text.toString().trim(),
                birthDate = binding.birthDateTiet.text.toString().trim()
            )
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.saveState.collect { state ->
                    when (state) {
                        is ResponseService.Loading -> {
                            communicator.manageLoader(true)
                            binding.btnContinuar.isEnabled = false
                        }
                        is ResponseService.Success -> {
                            communicator.manageLoader(false)
                            val intent = Intent(requireContext(), HomeActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                        is ResponseService.Error -> {
                            communicator.manageLoader(false)
                            binding.btnContinuar.isEnabled = true
                            Snackbar.make(binding.root, state.error, Snackbar.LENGTH_LONG).show()
                        }
                        null -> Unit
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}