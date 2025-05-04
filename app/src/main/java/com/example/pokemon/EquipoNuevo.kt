package com.example.pokemon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class EquipoNuevo : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val listaPokemon = mutableListOf<Pokemon>()
    private lateinit var adapter: PokemonAdapter

    private lateinit var slots: List<ImageView>
    private var contadorSlots = 0

    private val seleccionados = mutableListOf<String>()

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
                seleccionados.add(pokemon.nombre)
                contadorSlots++
            }
        }

        recyclerView.adapter = adapter
        cargarPokemons()

        val btnGuardar = view.findViewById<Button>(R.id.btnGuardarEquipo)
        btnGuardar.setOnClickListener {
            if (seleccionados.size == 6) {
                val equipoMap = HashMap<String, String>()
                for (i in 0 until 6) {
                    equipoMap["pokemon${i + 1}"] = seleccionados[i]
                }

                FirebaseFirestore.getInstance().collection("equipos")
                    .add(equipoMap)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Equipo guardado", Toast.LENGTH_SHORT).show()
                        parentFragmentManager.popBackStack()
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Error al guardar equipo", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(requireContext(), "Selecciona 6 Pokémon", Toast.LENGTH_SHORT).show()
            }
        }
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
            { }
        )

        Volley.newRequestQueue(requireContext()).add(request)
    }
}
