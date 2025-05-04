package com.example.pokemon

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokemon.databinding.FragmentFavoritosBinding

class FavoritosFragment : Fragment() {

    private lateinit var binding: FragmentFavoritosBinding
    private val favoritos = mutableListOf<Pokemon>()
    private lateinit var adapter: PokemonAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerFavoritos.layoutManager = LinearLayoutManager(requireContext())

        adapter = PokemonAdapter(favoritos) {}
        binding.recyclerFavoritos.adapter = adapter

        cargarFavoritos()
    }

    private fun cargarFavoritos() {
        val prefs = requireContext().getSharedPreferences("Favoritos", Context.MODE_PRIVATE)
        favoritos.clear()
        for (i in 1..151) {
            val nombre = "pokemon_$i"
            if (prefs.getBoolean(nombre, false)) {
                val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$i.png"
                favoritos.add(Pokemon(nombre, url))
            }
        }
        adapter.notifyDataSetChanged()
    }
}