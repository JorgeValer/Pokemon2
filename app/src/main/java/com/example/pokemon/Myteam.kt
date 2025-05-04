package com.example.pokemon

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class Myteam : Fragment() {
    private val TAG = "Myteam"

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EquipoAdapter
    private lateinit var firestore: FirebaseFirestore
    private val listaEquipos = mutableListOf<Equipo>()
    private var scrollPosition = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")
        return inflater.inflate(R.layout.mi_equipo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")

        // Restaurar estado si existe
        if (savedInstanceState != null) {
            Log.d(TAG, "Restoring saved state")
            scrollPosition = savedInstanceState.getInt("SCROLL_POSITION", 0)
        }

        recyclerView = view.findViewById(R.id.recyclerEquipo)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = EquipoAdapter(listaEquipos) { equipo ->
            eliminarEquipo(equipo)
        }

        recyclerView.adapter = adapter

        view.findViewById<Button>(R.id.btnCrearEquipo).setOnClickListener {
            Log.d(TAG, "Navegando a crear equipo")
            parentFragmentManager.beginTransaction()
                .replace(R.id.navbar_fragment_container, EquipoNuevo())
                .addToBackStack(null)
                .commit()
        }

        firestore = FirebaseFirestore.getInstance()
        cargarEquipos()
    }

    private fun cargarEquipos() {
        Log.d(TAG, "Cargando equipos desde Firestore")
        firestore.collection("equipos").get()
            .addOnSuccessListener { result ->
                listaEquipos.clear()
                for (document in result) {
                    val equipo = Equipo(
                        id = 0,
                        nombre = document.getString("nombre") ?: "",
                        ciudad = document.getString("ciudad") ?: "",
                        idDocumento = document.id
                    )
                    listaEquipos.add(equipo)
                }
                adapter.notifyDataSetChanged()
                Log.d(TAG, "Cargados ${listaEquipos.size} equipos")

                // Restaurar posición de desplazamiento
                if (scrollPosition > 0) {
                    (recyclerView.layoutManager as LinearLayoutManager).scrollToPosition(scrollPosition)
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error al cargar equipos: ${e.message}")
                Toast.makeText(requireContext(), "Error al cargar equipos", Toast.LENGTH_SHORT).show()
            }
    }

    private fun eliminarEquipo(equipo: Equipo) {
        Log.d(TAG, "Eliminando equipo: ${equipo.nombre}")
        firestore.collection("equipos").document(equipo.idDocumento)
            .delete()
            .addOnSuccessListener {
                listaEquipos.remove(equipo)
                adapter.notifyDataSetChanged()
                Toast.makeText(requireContext(), "Equipo eliminado", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error al eliminar equipo: ${e.message}")
                Toast.makeText(requireContext(), "Error al eliminar", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        // Al volver a la lista, recargar equipos para actualizar cambios
        cargarEquipos()
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