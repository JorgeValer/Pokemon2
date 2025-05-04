package com.example.pokemon

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Torneo(
    private val listaTorneos: List<ModeloTorneo>,
    private val onItemClick: (ModeloTorneo) -> Unit
) : RecyclerView.Adapter<Torneo.TorneoViewHolder>() {

    class TorneoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.tvNombreTorneo)
        val ciudad: TextView = view.findViewById(R.id.tvCiudad)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TorneoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.torneo, parent, false)
        return TorneoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TorneoViewHolder, position: Int) {
        val torneo = listaTorneos[position]
        holder.nombre.text = torneo.nombre
        holder.ciudad.text = torneo.ciudad

        holder.itemView.setOnClickListener {
            onItemClick(torneo)
        }
    }

    override fun getItemCount(): Int = listaTorneos.size
}
