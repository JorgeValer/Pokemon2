package com.example.pokemon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.pokemon.databinding.DetailBinding
import com.google.firebase.firestore.FirebaseFirestore

class Detail : Fragment() {

    private lateinit var binding: DetailBinding
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val nombre = arguments?.getString("nombre") ?: ""
        val ciudad = arguments?.getString("ciudad") ?: ""

        val imagenUrl = "https://assets.pokemon.com/assets/cms2/img/misc/virtual-console/vc-pokemon-yellow.png"

        binding.tvNombreDetalle.text = nombre
        binding.tvCiudadDetalle.text = ciudad

        Glide.with(requireContext())
            .load(imagenUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_foreground)
            .into(binding.imageDetalle)

        firestore = FirebaseFirestore.getInstance()

        binding.btnGuardarEquipo.setOnClickListener {
            val nuevoEquipo = hashMapOf(
                "nombre" to nombre,
                "ciudad" to ciudad
            )

            firestore.collection("equipos")
                .add(nuevoEquipo)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Guardado en Firestore", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Error al guardar", Toast.LENGTH_SHORT).show()
                }
        }
    }
}