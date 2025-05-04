package com.example.pokemon

import android.content.Context
import android.os.Bundle
import android.util.Log
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
    private val TAG = "EquipoNuevo"

    private lateinit var recyclerView: RecyclerView
    private val listaPokemon = mutableListOf<Pokemon>()
    private lateinit var adapter: PokemonAdapter

    private lateinit var slots: List<ImageView>
    private var contadorSlots = 0
    private val pokemonSeleccionadosIds = mutableListOf<String>() // Para guardar IDs
    private var scrollPosition = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")
        return inflater.inflate(R.layout.fragment_crear, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")

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

        // Restaurar estado si existe
        if (savedInstanceState != null) {
            Log.d(TAG, "Restoring saved state")
            contadorSlots = savedInstanceState.getInt("CONTADOR_SLOTS", 0)
            scrollPosition = savedInstanceState.getInt("SCROLL_POSITION", 0)
            val ids = savedInstanceState.getStringArrayList("POKEMON_IDS")
            if (ids != null) {
                pokemonSeleccionadosIds.addAll(ids)
                // Nota: los slots visuales se actualizarán después de cargar los datos
            }
        }

        adapter = PokemonAdapter(listaPokemon) { pokemon ->
            if (contadorSlots < 6) {
                Log.d(TAG, "Pokemon seleccionado: ${pokemon.nombre}")
                Glide.with(requireContext())
                    .load(pokemon.imagenUrl)
                    .into(slots[contadorSlots])

                // Guardar el ID/nombre del Pokémon seleccionado
                pokemonSeleccionadosIds.add(pokemon.nombre)

                contadorSlots++
            }
        }

        recyclerView.adapter = adapter

        val btnFavoritos = view.findViewById<Button>(R.id.btnFavoritos)
        btnFavoritos.setOnClickListener {
            Log.d(TAG, "Navegando a favoritos")
            parentFragmentManager.beginTransaction()
                .replace(R.id.navbar_fragment_container, FavoritosFragment())
                .addToBackStack(null)
                .commit()
        }

        cargarPokemons()
    }

    // Restaurar los slots después de que los datos estén cargados
    private fun restaurarSlots() {
        Log.d(TAG, "Restaurando ${pokemonSeleccionadosIds.size} slots guardados")
        for (i in 0 until minOf(contadorSlots, pokemonSeleccionadosIds.size)) {
            val pokemonId = pokemonSeleccionadosIds[i]
            // Buscar el Pokémon en la lista por su nombre
            val pokemon = listaPokemon.find { it.nombre == pokemonId }
            if (pokemon != null) {
                Glide.with(requireContext())
                    .load(pokemon.imagenUrl)
                    .into(slots[i])
            }
        }
    }

    private fun cargarPokemons() {
        Log.d(TAG, "Cargando lista de Pokémon")
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
                Log.d(TAG, "Cargados ${listaPokemon.size} Pokémon")

                // Restaurar posición de desplazamiento
                if (scrollPosition > 0) {
                    (recyclerView.layoutManager as LinearLayoutManager).scrollToPosition(scrollPosition)
                }

                // Restaurar slots visuales después de cargar los datos
                if (pokemonSeleccionadosIds.isNotEmpty()) {
                    restaurarSlots()
                }
            },
            { error ->
                Log.e(TAG, "Error al cargar Pokémon: ${error.message}")
            }
        )

        Volley.newRequestQueue(requireContext()).add(request)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState: guardando $contadorSlots slots")

        // Guardar contadorSlots y pokemonSeleccionadosIds
        outState.putInt("CONTADOR_SLOTS", contadorSlots)
        outState.putStringArrayList("POKEMON_IDS", ArrayList(pokemonSeleccionadosIds))

        // Guardar posición de desplazamiento
        if (::recyclerView.isInitialized && recyclerView.layoutManager != null) {
            val position = (recyclerView.layoutManager as LinearLayoutManager)
                .findFirstVisibleItemPosition()
            outState.putInt("SCROLL_POSITION", position)
        }
    }
}