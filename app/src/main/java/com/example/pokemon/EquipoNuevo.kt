package com.example.pokemon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.pokemon.databinding.FragmentCrearBinding

class EquipoNuevo : Fragment() {

    private lateinit var binding: FragmentCrearBinding
    private val listaPokemon = mutableListOf<Pokemon>()
    private lateinit var adapter: PokemonAdapter

    private var contadorSlots = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCrearBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerPokemon.layoutManager = LinearLayoutManager(requireContext())

        val slots = listOf(
            binding.slot1,
            binding.slot2,
            binding.slot3,
            binding.slot4,
            binding.slot5,
            binding.slot6
        )

        adapter = PokemonAdapter(listaPokemon) { pokemon ->
            if (contadorSlots < 6) {
                Glide.with(requireContext())
                    .load(pokemon.imagenUrl)
                    .into(slots[contadorSlots])
                contadorSlots++
            }
        }

        binding.recyclerPokemon.adapter = adapter

        binding.btnFavoritos.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.navbar_fragment_container, FavoritosFragment())
                .addToBackStack(null)
                .commit()
        }

        cargarPokemons()
    }

    private fun cargarPokemons() {
        val url = "https://pokeapi.co/api/v2/pokemon?limit=151"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                val results = response.getJSONArray("results")
                for (i in 0 until results.length()) {
                    val pokemonObj = results.getJSONObject(i)
                    val nombre = pokemonObj.getString("name")
                    val urlDetalle = pokemonObj.getString("url")
                    val id = urlDetalle.trimEnd('/').split("/").last()
                    val imagenUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
                    listaPokemon.add(Pokemon(nombre, imagenUrl))
                }
                adapter.notifyDataSetChanged()
            },
            {}
        )

        Volley.newRequestQueue(requireContext()).add(request)
    }
}