package com.example.pokemon

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class InicioFragment : Fragment() {
    private val TAG = "InicioFragment"
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Torneo
    private var torneosList = listOf(
        ModeloTorneo("VGC Regional Madrid", "Madrid"),
        ModeloTorneo("VGC Open Londres", "Londres"),
        ModeloTorneo("VGC Internacional Japón", "Tokio")
    )
    private var scrollPosition = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")
        return inflater.inflate(R.layout.home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")

        // Restaurar estado si existe
        if (savedInstanceState != null) {
            Log.d(TAG, "Restoring saved state")
            scrollPosition = savedInstanceState.getInt("SCROLL_POSITION", 0)
            // También podrías restaurar la lista de torneos si la guardaste
        }

        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerTorneos)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Método mejorado para comunicación de actividades/fragmentos
        adapter = Torneo(torneosList) { torneo ->
            Log.d(TAG, "Torneo seleccionado: ${torneo.nombre}")

            // Opción 1: Usando directamente fragmentManager (tu método actual)
            val bundle = Bundle().apply {
                putString("nombre", torneo.nombre)
                putString("ciudad", torneo.ciudad)
            }
            val detalle = Detail()
            detalle.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.navbar_fragment_container, detalle)
                .addToBackStack(null)
                .commit()

            // Opción 2: Usando Intent (alternativa para actividades)
            /*
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("nombre", torneo.nombre)
            intent.putExtra("ciudad", torneo.ciudad)
            startActivity(intent)
            */
        }

        recyclerView.adapter = adapter

        // Restaurar posición de desplazamiento
        (recyclerView.layoutManager as LinearLayoutManager).scrollToPosition(scrollPosition)
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

    // Método para compartir torneos usando createChooser
    private fun compartirTorneo(torneo: ModeloTorneo) {
        val mensaje = "¡Mira este torneo Pokémon: ${torneo.nombre} en ${torneo.ciudad}!"

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, mensaje)

        val chooserIntent = Intent.createChooser(intent, "Compartir torneo")
        startActivity(chooserIntent)
    }
}