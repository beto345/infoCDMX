package com.example.infocdmx.home.transporte

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infocdmx.databinding.FragmentTransporteBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

class TransporteFragment : Fragment() {

    private var _binding: FragmentTransporteBinding? = null
    private val binding get() = _binding!!
    private val adapter = TransportAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransporteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadTransportData()
    }

    private fun setupRecyclerView() {
        binding.rvTransport.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTransport.adapter = adapter
    }

    private fun loadTransportData() {
        try {
            val inputStream = requireContext().assets.open("transporte.json")
            val reader = InputStreamReader(inputStream)
            val type = object : TypeToken<List<TransportLine>>() {}.type
            val list: List<TransportLine> = Gson().fromJson(reader, type)
            adapter.submitList(list)
            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
