package com.example.pokemon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokemon.databinding.HomeBinding

class InicioFragment : Fragment() {

    private lateinit var binding: HomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerTorneos.layoutManager = LinearLayoutManager(requireContext())

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

        binding.recyclerTorneos.adapter = adapter
    }
}