package com.example.pokemon

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemon.databinding.TorneoBinding

class Torneo(
    private val listaTorneos: List<ModeloTorneo>,
    private val onItemClick: (ModeloTorneo) -> Unit
) : RecyclerView.Adapter<Torneo.TorneoViewHolder>() {

    class TorneoViewHolder(val binding: TorneoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TorneoViewHolder {
        val binding = TorneoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TorneoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TorneoViewHolder, position: Int) {
        val torneo = listaTorneos[position]

        with(holder.binding) {
            tvNombreTorneo.text = torneo.nombre
            tvCiudad.text = torneo.ciudad

            root.setOnClickListener {
                onItemClick(torneo)
            }
        }
    }

    override fun getItemCount(): Int = listaTorneos.size
}