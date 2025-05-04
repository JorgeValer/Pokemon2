package com.example.pokemon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.pokemon.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: Torneo
    private val listaTorneos = mutableListOf<ModeloTorneo>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = Torneo(listaTorneos) { torneo ->
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

        binding.recyclerEquipo.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerEquipo.adapter = adapter

        cargarTorneosDesdeVolley()
    }

    private fun cargarTorneosDesdeVolley() {
        val url = "https://jsonkeeper.com/b/1T4R"

        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                listaTorneos.clear()
                for (i in 0 until response.length()) {
                    val obj = response.getJSONObject(i)
                    val nombre = obj.getString("nombre")
                    val ciudad = obj.getString("ciudad")
                    listaTorneos.add(ModeloTorneo(nombre, ciudad))
                }
                adapter.notifyDataSetChanged()
            },
            {
                Toast.makeText(requireContext(), "Error al cargar torneos", Toast.LENGTH_SHORT).show()
            }
        )

        Volley.newRequestQueue(requireContext()).add(request)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
