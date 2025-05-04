package com.example.pokemon

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide

class EquipoNuevo : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val listaPokemon = mutableListOf<Pokemon>()
    private lateinit var adapter: PokemonAdapter

    private lateinit var slots: List<ImageView>
    private var contadorSlots = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_crear, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.recyclerPokemon)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        slots = listOf(
            view.findViewById(R.id.slot1),
            view.findViewById(R.id.slot2),
            view.findViewById(R.id.slot3),
            view.findViewById(R.id.slot4),
            view.findViewById(R.id.slot5),
            view.findViewById(R.id.slot6)
        )

        adapter = PokemonAdapter(listaPokemon) { pokemon ->
            if (contadorSlots < 6) {
                Glide.with(requireContext())
                    .load(pokemon.imagenUrl)
                    .into(slots[contadorSlots])
                contadorSlots++
            }
        }

        recyclerView.adapter = adapter

        val btnFavoritos = view.findViewById<Button>(R.id.btnFavoritos)
        btnFavoritos.setOnClickListener {
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
