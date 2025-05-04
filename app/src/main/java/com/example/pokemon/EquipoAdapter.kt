package com.example.pokemon

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemon.databinding.EquiposBinding

class EquipoAdapter(
    private val listaEquipos: MutableList<Equipo>,
    private val onEliminarClick: (Equipo) -> Unit
) : RecyclerView.Adapter<EquipoAdapter.EquipoViewHolder>() {

    class EquipoViewHolder(val binding: EquiposBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipoViewHolder {
        val binding = EquiposBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EquipoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EquipoViewHolder, position: Int) {
        val equipoActual = listaEquipos[position]

        with(holder.binding) {
            tvNombre.text = equipoActual.nombre
            tvCiudad.text = equipoActual.ciudad

            btnEliminar.setOnClickListener {
                onEliminarClick(equipoActual)
            }
        }
    }

    override fun getItemCount(): Int = listaEquipos.size
}