package com.example.infocdmx.home.estaciones

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.infocdmx.databinding.ItemEstacionBinding

class EstacionAdapter(
    private val colorHex: String
) : ListAdapter<TransportStation, EstacionAdapter.VH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemEstacionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    inner class VH(private val b: ItemEstacionBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(est: TransportStation) {
            b.tvEstacionNombre.text = est.nombre
            val partes = listOfNotNull(
                est.tipo.ifBlank { null },
                est.alcaldia.ifBlank { null },
                if (est.esCetram) "CETRAM" else null
            )
            b.tvEstacionSub.text = partes.joinToString(" · ")
            val color = runCatching { Color.parseColor(colorHex) }.getOrDefault(Color.GRAY)
            b.viewEstacionDot.setBackgroundColor(color)
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<TransportStation>() {
            override fun areItemsTheSame(o: TransportStation, n: TransportStation) = o.id == n.id
            override fun areContentsTheSame(o: TransportStation, n: TransportStation) = o == n
        }
    }
}