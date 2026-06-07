package com.example.infocdmx.home.placeDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.infocdmx.core.model.Place
import com.example.infocdmx.databinding.FragmentPlaceDetailBinding

class PlaceDetailFragment : Fragment() {

    private var _binding: FragmentPlaceDetailBinding? = null
    private val binding get() = _binding!!
    private var place: Place? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        place = arguments?.getParcelable("place")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaceDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        place?.let {
            binding.tvTitle.text = it.name
            binding.tvAddress.text = it.formattedAddress ?: it.vicinity ?: ""
            binding.tvRating.text = "Rating: ${it.rating ?: "N/A"}"
            binding.tvStatus.text = it.businessStatus ?: ""

            Glide.with(requireContext())
                .load(it.icon)
                .placeholder(com.example.infocdmx.R.drawable.ic_place)
                .error(com.example.infocdmx.R.drawable.ic_place)
                .centerCrop()
                .into(binding.ivCover)
        }

        binding.btnClose.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
