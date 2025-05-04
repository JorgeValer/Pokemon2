package com.example.pokemon

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")

        try {
            // Initialize Firebase first - before setContentView
            FirebaseApp.initializeApp(this)

            // Set the content view - this can throw an exception if layout has issues
            setContentView(R.layout.activity_main)

            Log.d(TAG, "Layout loaded successfully")

            // Restaurar estado si existe
            if (savedInstanceState != null) {
                Log.d(TAG, "Restoring saved state")
                // Aquí puedes recuperar datos guardados si es necesario
            }

            // Use a simpler approach to load the initial fragment
            if (savedInstanceState == null) {
                Log.d(TAG, "Loading initial fragment")
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_main, Login())
                    .commit()

                Log.d(TAG, "Login fragment loaded")
            }

        } catch (e: Exception) {
            // Log any exceptions
            Log.e(TAG, "Error in onCreate: ${e.message}", e)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
        // La actividad está a punto de hacerse visible
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        // La actividad está visible y tiene el foco
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
        // La actividad está parcialmente visible o pierde el foco
        // Buen lugar para pausar animaciones, operaciones de red, etc.
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
        // La actividad ya no es visible
        // Liberar recursos que no son necesarios cuando no es visible
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart")
        // La actividad va a empezar de nuevo después de haber sido detenida
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
        // La actividad será destruida
        // Realizar limpieza final de recursos
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(TAG, "onSaveInstanceState")
        // Guardar el estado que se necesite restaurar después
        // por ejemplo, la navegación, selecciones del usuario, etc.

        super.onSaveInstanceState(outState)
    }
}