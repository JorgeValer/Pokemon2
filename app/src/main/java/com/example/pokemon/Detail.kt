package com.example.pokemon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class Detail : Fragment() {

    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val nombre = arguments?.getString("nombre") ?: ""
        val ciudad = arguments?.getString("ciudad") ?: ""

        val imagenUrl = "https://assets.pokemon.com/assets/cms2/img/misc/virtual-console/vc-pokemon-yellow.png"

        val tvNombre = view.findViewById<TextView>(R.id.tvNombreDetalle)
        val tvCiudad = view.findViewById<TextView>(R.id.tvCiudadDetalle)
        val imageView = view.findViewById<ImageView>(R.id.imageDetalle)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardarEquipo)

        tvNombre.text = nombre
        tvCiudad.text = ciudad


        Glide.with(requireContext())
            .load(imagenUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_foreground)
            .into(imageView)


        firestore = FirebaseFirestore.getInstance()

        btnGuardar.setOnClickListener {
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
