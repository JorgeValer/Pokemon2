package com.example.pokemon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class InicioFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerTorneos)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val torneos = listOf(
            ModeloTorneo("VGC Regional Madrid", "Madrid"),
            ModeloTorneo("VGC Open Londres", "Londres"),
            ModeloTorneo("VGC Internacional Japón", "Tokio")
        )

        val adapter = Torneo(torneos) { torneo ->
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
        }

        recyclerView.adapter = adapter
    }
}