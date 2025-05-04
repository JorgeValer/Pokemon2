package com.example.pokemon

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokemon.databinding.PokemonBinding

class PokemonAdapter(
    private val lista: List<Pokemon>,
    private val onItemClick: (Pokemon) -> Unit
) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    class PokemonViewHolder(val binding: PokemonBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val binding = PokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = lista[position]
        val context = holder.itemView.context

        with(holder.binding) {
            tvNombrePokemon.text = pokemon.nombre.replaceFirstChar { it.uppercase() }

            Glide.with(context)
                .load(pokemon.imagenUrl)
                .into(ivImagenPokemon)

            val prefs = context.getSharedPreferences("Favoritos", Context.MODE_PRIVATE)
            val key = pokemon.nombre
            val esFavorito = prefs.getBoolean(key, false)
            ivFavorito.setImageResource(
                if (esFavorito) R.drawable.ic_star_filled else R.drawable.ic_star_border
            )

            ivFavorito.setOnClickListener {
                val nuevoEstado = !prefs.getBoolean(key, false)
                prefs.edit().putBoolean(key, nuevoEstado).apply()
                notifyItemChanged(position)
            }

            root.setOnClickListener {
                onItemClick(pokemon)
            }
        }
    }

    override fun getItemCount(): Int = lista.size
}