package com.example.pokemon

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EquipoAdapter(
    private val listaEquipos: MutableList<Equipo>,
    private val onEliminarClick: (Equipo) -> Unit
) : RecyclerView.Adapter<EquipoAdapter.EquipoViewHolder>() {

    class EquipoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.tvNombre)
        val ciudad: TextView = view.findViewById(R.id.tvCiudad)
        val btnEliminar: Button = view.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipoViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.equipos, parent, false)
        return EquipoViewHolder(vista)
    }

    override fun onBindViewHolder(holder: EquipoViewHolder, position: Int) {
        val equipoActual = listaEquipos[position]
        holder.nombre.text = equipoActual.nombre
        holder.ciudad.text = equipoActual.ciudad

        holder.btnEliminar.setOnClickListener {
            onEliminarClick(equipoActual)
        }
    }

    override fun getItemCount(): Int = listaEquipos.size
}