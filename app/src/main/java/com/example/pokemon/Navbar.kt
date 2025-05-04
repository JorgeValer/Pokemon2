package com.example.pokemon

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class Navbar : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navbar)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Fragmento por defecto: Inicio
        supportFragmentManager.beginTransaction()
            .replace(R.id.navbar_fragment_container, InicioFragment())
            .commit()

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_mercado -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.navbar_fragment_container, MercadoFragment())
                        .commit()
                    true
                }
                R.id.nav_equipo -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.navbar_fragment_container, Myteam())
                        .commit()
                    true
                }
                R.id.nav_inicio -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.navbar_fragment_container, InicioFragment())
                        .commit()
                    true
                }
                R.id.nav_clasificacion -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.navbar_fragment_container, ClasificacionFragment())
                        .commit()
                    true
                }
                R.id.nav_chat -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.navbar_fragment_container, ChatFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }
}
