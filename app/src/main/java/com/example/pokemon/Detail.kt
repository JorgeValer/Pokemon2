package com.example.pokemon

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class Detail : Fragment() {
    private val TAG = "Detail"
    private lateinit var firestore: FirebaseFirestore
    private lateinit var tvNombre: TextView
    private lateinit var tvCiudad: TextView
    private lateinit var imageView: ImageView
    private lateinit var btnGuardar: Button
    private lateinit var btnCompartir: Button

    private var nombre: String = ""
    private var ciudad: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")
        return inflater.inflate(R.layout.detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")

        // Recuperar datos guardados si existen
        if (savedInstanceState != null) {
            Log.d(TAG, "Restoring saved state")
            nombre = savedInstanceState.getString("nombre", "")
            ciudad = savedInstanceState.getString("ciudad", "")
        } else {
            // Obtener datos de los argumentos
            nombre = arguments?.getString("nombre") ?: ""
            ciudad = arguments?.getString("ciudad") ?: ""
        }

        Log.d(TAG, "Mostrando detalles de: $nombre en $ciudad")

        val imagenUrl = "https://assets.pokemon.com/assets/cms2/img/misc/virtual-console/vc-pokemon-yellow.png"

        tvNombre = view.findViewById<TextView>(R.id.tvNombreDetalle)
        tvCiudad = view.findViewById<TextView>(R.id.tvCiudadDetalle)
        imageView = view.findViewById<ImageView>(R.id.imageDetalle)
        btnGuardar = view.findViewById<Button>(R.id.btnGuardarEquipo)

        // Agregar botón para compartir (añádelo a tu layout)
        btnCompartir = view.findViewById<Button>(R.id.btnCompartir) // Debes añadir este botón a tu layout detail.xml

        tvNombre.text = nombre
        tvCiudad.text = ciudad

        Glide.with(requireContext())
            .load(imagenUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_foreground)
            .into(imageView)

        firestore = FirebaseFirestore.getInstance()

        btnGuardar.setOnClickListener {
            guardarEnFirestore()
        }

        btnCompartir.setOnClickListener {
            compartirTorneo()
        }
    }

    private fun guardarEnFirestore() {
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

    // Método para compartir usando createChooser
    private fun compartirTorneo() {
        val mensaje = "¡Mira este torneo Pokémon: $nombre en $ciudad!"

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, mensaje)
        intent.putExtra(Intent.EXTRA_SUBJECT, "Información de torneo Pokémon")

        val chooserIntent = Intent.createChooser(intent, "Compartir torneo")
        startActivity(chooserIntent)
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

        // Guardar el estado actual
        outState.putString("nombre", nombre)
        outState.putString("ciudad", ciudad)
    }
}