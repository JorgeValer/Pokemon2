package com.example.pokemon

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PokemonAdapter(
    private val lista: List<Pokemon>,
    private val onItemClick: (Pokemon) -> Unit
) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    class PokemonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.tvNombrePokemon)
        val imagen: ImageView = view.findViewById(R.id.ivImagenPokemon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pokemon, parent, false) // ← aquí está el cambio
        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = lista[position]
        holder.nombre.text = pokemon.nombre.replaceFirstChar { it.uppercase() }

        Glide.with(holder.itemView.context)
            .load(pokemon.imagenUrl)
            .into(holder.imagen)

        holder.itemView.setOnClickListener {
            onItemClick(pokemon)
        }
    }

    override fun getItemCount(): Int = lista.size
}
