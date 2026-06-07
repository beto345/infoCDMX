package com.example.infocdmx.home.places

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infocdmx.R
import com.example.infocdmx.core.FragmentCommunicator
import com.example.infocdmx.core.ResponseService
import com.example.infocdmx.databinding.FragmentLugaresBinding
import com.example.infocdmx.home.lugares.PlaceViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import androidx.navigation.fragment.findNavController

class LugaresFragment : Fragment() {

    private var _binding: FragmentLugaresBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlaceViewModel by viewModels()

    private lateinit var communicator: FragmentCommunicator

    private val adapter = PlaceAdapter { place ->
        val bundle = Bundle().apply { 
            putParcelable("place", place) 
        }
        findNavController().navigate(R.id.action_lugaresFragment_to_placeDetailFragment, bundle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLugaresBinding.inflate(inflater, container, false)
        communicator = requireActivity() as FragmentCommunicator
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeState()
        viewModel.loadPlaces(requireContext())
    }

    private fun setupRecyclerView() {
        binding.rvLugares.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLugares.adapter = adapter
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.placeState.collect { state ->
                    when (state) {
                        is ResponseService.Loading -> {
                            communicator.manageLoader(isVisible = true)
                        }

                        is ResponseService.Success -> {
                            communicator.manageLoader(isVisible = false)
                            adapter.submitList(state.data)
                            Log.i("Lugares", "Places List: ${state.data}")
                        }

                        is ResponseService.Error -> {
                            communicator.manageLoader(isVisible = false)
                            Snackbar.make(binding.root, state.error, Snackbar.LENGTH_LONG).show()
                        }

                        null -> {}
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
