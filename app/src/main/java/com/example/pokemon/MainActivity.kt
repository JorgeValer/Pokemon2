package com.example.pokemon

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.pokemon.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            // Initialize Firebase first - before setContentView
            FirebaseApp.initializeApp(this)

            // Initialize View Binding
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            Log.d(TAG, "Layout loaded successfully")

            // Let the NavHostFragment handle the navigation
            if (savedInstanceState == null) {
                try {
                    val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_main) as? NavHostFragment
                    Log.d(TAG, "NavHostFragment loaded: ${navHostFragment != null}")
                } catch (e: Exception) {
                    Log.e(TAG, "Error setting up navigation: ${e.message}", e)

                    // Fallback to manual fragment loading if NavHostFragment isn't working
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_main, Login())
                        .commit()
                    Log.d(TAG, "Manually loaded Login fragment")
                }
            }
        } catch (e: Exception) {
            // Log any exceptions
            Log.e(TAG, "Error in onCreate: ${e.message}", e)
        }
    }
}