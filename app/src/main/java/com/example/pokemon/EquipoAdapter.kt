package com.example.pokemon

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EquipoAdapter(
    private val lista: MutableList<Equipo>,
    private val onEliminarClick: (Equipo) -> Unit
) : RecyclerView.Adapter<EquipoAdapter.EquipoViewHolder>() {

    class EquipoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.tvNombreEquipo)
        val ciudad: TextView = view.findViewById(R.id.tvCiudadEquipo)
        val btnEliminar: Button = view.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipoViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.equipo, parent, false)
        return EquipoViewHolder(vista)
    }

    override fun onBindViewHolder(holder: EquipoViewHolder, position: Int) {
        val equipo = lista[position]
        holder.nombre.text = equipo.nombre
        holder.ciudad.text = equipo.ciudad

        holder.btnEliminar.setOnClickListener {
            onEliminarClick(equipo)
        }
    }

    override fun getItemCount(): Int = lista.size
}
