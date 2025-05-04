package com.example.pokemon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class home : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
            findNavController().navigate(R.id.action_home_to_detail, bundle)
        }

        recyclerView.adapter = adapter
    }
}
