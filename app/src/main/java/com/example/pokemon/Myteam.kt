package com.example.pokemon

import android.os.Bundle
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

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EquipoAdapter
    private lateinit var firestore: FirebaseFirestore
    private val listaEquipos = mutableListOf<EquipoVisual>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.mi_equipo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.recyclerEquipo)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = EquipoAdapter(listaEquipos) { equipo ->
            eliminarEquipo(equipo)
        }

        recyclerView.adapter = adapter

        view.findViewById<Button>(R.id.btnCrearEquipo).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.navbar_fragment_container, EquipoNuevo())
                .addToBackStack(null)
                .commit()
        }

        firestore = FirebaseFirestore.getInstance()
        cargarEquipos()
    }

    private fun cargarEquipos() {
        firestore.collection("equipos").get()
            .addOnSuccessListener { result ->
                listaEquipos.clear()
                for (document in result) {
                    val pokes = (1..6).mapNotNull { document.getString("pokemon$it") }
                    val equipo = EquipoVisual(pokes, document.id)
                    listaEquipos.add(equipo)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al cargar equipos", Toast.LENGTH_SHORT).show()
            }
    }

    private fun eliminarEquipo(equipo: EquipoVisual) {
        firestore.collection("equipos").document(equipo.idDocumento)
            .delete()
            .addOnSuccessListener {
                listaEquipos.remove(equipo)
                adapter.notifyDataSetChanged()
                Toast.makeText(requireContext(), "Equipo eliminado", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al eliminar", Toast.LENGTH_SHORT).show()
            }
    }
}
