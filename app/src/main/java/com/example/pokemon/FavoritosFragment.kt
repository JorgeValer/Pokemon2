package com.example.pokemon

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FavoritosFragment : Fragment() {
    private val TAG = "FavoritosFragment"

    private lateinit var recyclerView: RecyclerView
    private val favoritos = mutableListOf<Pokemon>()
    private lateinit var adapter: PokemonAdapter
    private var scrollPosition = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")
        return inflater.inflate(R.layout.fragment_favoritos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")

        // Restaurar estado si existe
        if (savedInstanceState != null) {
            Log.d(TAG, "Restoring saved state")
            scrollPosition = savedInstanceState.getInt("SCROLL_POSITION", 0)
        }

        recyclerView = view.findViewById(R.id.recyclerFavoritos)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = PokemonAdapter(favoritos) {}
        recyclerView.adapter = adapter

        cargarFavoritos()
    }

    private fun cargarFavoritos() {
        Log.d(TAG, "Cargando favoritos")
        val prefs = requireContext().getSharedPreferences("Favoritos", Context.MODE_PRIVATE)
        favoritos.clear()
        var contadorFavoritos = 0

        for (i in 1..151) {
            val nombre = "pokemon_$i"
            if (prefs.getBoolean(nombre, false)) {
                val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$i.png"
                favoritos.add(Pokemon(nombre, url))
                contadorFavoritos++
            }
        }

        Log.d(TAG, "Cargados $contadorFavoritos favoritos")
        adapter.notifyDataSetChanged()

        // Restaurar posición de desplazamiento
        if (scrollPosition > 0 && recyclerView.layoutManager != null) {
            (recyclerView.layoutManager as LinearLayoutManager).scrollToPosition(scrollPosition)
        }
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
        Log.d(TAG, "onSaveInstanceState")

        // Guardar posición de desplazamiento
        if (::recyclerView.isInitialized && recyclerView.layoutManager != null) {
            val position = (recyclerView.layoutManager as LinearLayoutManager)
                .findFirstVisibleItemPosition()
            outState.putInt("SCROLL_POSITION", position)
        }
    }
}