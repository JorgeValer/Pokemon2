package com.example.pokemon

import android.content.Context
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
        val favorito: ImageView = view.findViewById(R.id.ivFavorito)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pokemon, parent, false)
        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = lista[position]
        val context = holder.itemView.context

        holder.nombre.text = pokemon.nombre.capitalize()

        Glide.with(context)
            .load(pokemon.imagenUrl)
            .into(holder.imagen)

        val prefs = context.getSharedPreferences("Favoritos", Context.MODE_PRIVATE)
        val key = pokemon.nombre
        val esFavorito = prefs.getBoolean(key, false)
        holder.favorito.setImageResource(
            if (esFavorito) R.drawable.ic_star_filled else R.drawable.ic_star_border
        )

        holder.favorito.setOnClickListener {
            val nuevoEstado = !prefs.getBoolean(key, false)
            prefs.edit().putBoolean(key, nuevoEstado).apply()
            notifyItemChanged(position)
        }

        holder.itemView.setOnClickListener {
            onItemClick(pokemon)
        }
    }

    override fun getItemCount(): Int = lista.size
}