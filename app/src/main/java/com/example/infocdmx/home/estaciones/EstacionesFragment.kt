package com.example.infocdmx.home.estaciones

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infocdmx.core.ResponseService
import com.example.infocdmx.databinding.FragmentEstacionesBinding
import kotlinx.coroutines.launch

class EstacionesFragment : Fragment() {

    private var _binding: FragmentEstacionesBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<EstacionesViewModel>()

    private var lineaId = 0
    private var sistema = "METRO"
    private var nombreLinea = ""
    private var colorHex = "#1F4E79"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireArguments().let {
            lineaId = it.getInt("lineaId", 0)
            sistema = it.getString("sistema", "METRO")
            nombreLinea = it.getString("nombreLinea", "")
            colorHex = it.getString("colorHex", "#1F4E79")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEstacionesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvLineaTitle.text = nombreLinea
        val color = runCatching { Color.parseColor(colorHex) }.getOrDefault(Color.GRAY)
        binding.viewLineColor.setBackgroundColor(color)

        val adapter = EstacionAdapter(colorHex)
        binding.rvEstaciones.layoutManager = LinearLayoutManager(requireContext())
        binding.rvEstaciones.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is ResponseService.Loading -> {
                            binding.estacionesProgress.isVisible = true
                            binding.tvEstacionesEmpty.isVisible = false
                        }
                        is ResponseService.Success -> {
                            binding.estacionesProgress.isVisible = false
                            binding.tvEstacionesEmpty.isVisible = false
                            binding.tvLineaTitle.text =
                                "$nombreLinea · ${state.data.size} estaciones"
                            adapter.submitList(state.data)
                        }
                        is ResponseService.Error -> {
                            binding.estacionesProgress.isVisible = false
                            binding.tvEstacionesEmpty.isVisible = true
                            binding.tvEstacionesEmpty.text = state.error
                        }
                    }
                }
            }
        }

        viewModel.cargar(sistema, lineaId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}