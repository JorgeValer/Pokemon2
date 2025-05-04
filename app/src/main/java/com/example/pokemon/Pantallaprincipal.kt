package com.example.pokemon

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.pokemon.databinding.ActivityPantallaprincipalBinding

class Pantallaprincipal : AppCompatActivity() {

    private lateinit var binding: ActivityPantallaprincipalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPantallaprincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load initial fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.navbar_fragment_container, InicioFragment())
            .commit()

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            var selectedFragment: Fragment? = null

            when (item.itemId) {
                R.id.nav_mercado -> {
                    selectedFragment = MercadoFragment()
                }
                R.id.nav_equipo -> {
                    selectedFragment = Myteam()
                }
                R.id.nav_inicio -> {
                    selectedFragment = InicioFragment()
                }
                R.id.nav_clasificacion -> {
                    selectedFragment = ClasificacionFragment()
                }
                R.id.nav_chat -> {
                    selectedFragment = ChatFragment()
                }
            }

            if (selectedFragment != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.navbar_fragment_container, selectedFragment)
                    .commit()
                true
            } else {
                false
            }
        }
    }
}