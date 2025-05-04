package com.example.pokemon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokemon.databinding.MiEquipoBinding
import com.google.firebase.firestore.FirebaseFirestore

class Myteam : Fragment() {

    private lateinit var binding: MiEquipoBinding
    private lateinit var adapter: EquipoAdapter
    private lateinit var firestore: FirebaseFirestore
    private val listaEquipos = mutableListOf<Equipo>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MiEquipoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerEquipo.layoutManager = LinearLayoutManager(requireContext())

        adapter = EquipoAdapter(listaEquipos) { equipo ->
            eliminarEquipo(equipo)
        }

        binding.recyclerEquipo.adapter = adapter

        binding.btnCrearEquipo.setOnClickListener {
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
                    val equipo = Equipo(
                        id = 0,
                        nombre = document.getString("nombre") ?: "",
                        ciudad = document.getString("ciudad") ?: "",
                        idDocumento = document.id
                    )
                    listaEquipos.add(equipo)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al cargar equipos", Toast.LENGTH_SHORT).show()
            }
    }

    private fun eliminarEquipo(equipo: Equipo) {
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