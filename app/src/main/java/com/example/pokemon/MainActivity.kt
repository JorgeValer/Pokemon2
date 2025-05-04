package com.example.pokemon

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            // Initialize Firebase first - before setContentView
            FirebaseApp.initializeApp(this)

            // Set the content view - this can throw an exception if layout has issues
            setContentView(R.layout.activity_main)

            Log.d("MainActivity", "Layout loaded successfully")

            // Use a simpler approach to load the initial fragment
            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_main, Login())
                    .commit()

                Log.d("MainActivity", "Login fragment loaded")
            }

        } catch (e: Exception) {
            // Log any exceptions
            Log.e("MainActivity", "Error in onCreate: ${e.message}", e)
        }
    }
}