package com.example.infocdmx.home.transporte

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.infocdmx.R
import com.example.infocdmx.core.ResponseService
import com.example.infocdmx.databinding.FragmentTransporteBinding
import kotlinx.coroutines.launch

class TransporteFragment : Fragment() {

    private var _binding: FragmentTransporteBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<TransporteViewModel>()

    private val adapter = TransportAdapter { line ->
        Toast.makeText(requireContext(), "Tocaste: ${line.name}", Toast.LENGTH_SHORT).show()
        val bundle = Bundle().apply {
            putInt("lineaId", line.lineaId)
            putString("sistema", line.sistemaCode)
            putString("nombreLinea", line.name)
            putString("colorHex", line.colorHex)
        }
        findNavController().navigate(R.id.action_homeFragment_to_estacionesFragment, bundle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransporteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvTransport.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        binding.rvTransport.adapter = adapter
        observeState()
        viewModel.cargarLineas()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is ResponseService.Loading -> {
                            binding.transportProgress.isVisible = true
                            binding.tvTransportEmpty.isVisible = false
                        }
                        is ResponseService.Success -> {
                            binding.transportProgress.isVisible = false
                            binding.tvTransportEmpty.isVisible = false
                            adapter.submitList(state.data)
                        }
                        is ResponseService.Error -> {
                            binding.transportProgress.isVisible = false
                            binding.tvTransportEmpty.isVisible = true
                            binding.tvTransportEmpty.text = state.error
                        }
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