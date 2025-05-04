package com.example.pokemon

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class Login : Fragment() {

    private lateinit var tilEmail: TextInputLayout
    private lateinit var tilPassword: TextInputLayout
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var sharedPreferences: SharedPreferences

    // Guardar estado para restaurar después de rotación
    private var emailText: String = ""
    private var isLoggingIn: Boolean = false

    companion object {
        const val TAG = "LoginFragment"
        const val PREF_NAME = "PokemonPrefs"
        const val KEY_IS_LOGGED_IN = "isLoggedIn"
        const val KEY_EMAIL = "email"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView called")
        try {
            return inflater.inflate(R.layout.login, container, false)
        } catch (e: Exception) {
            Log.e(TAG, "Error inflating layout: ${e.message}", e)
            // Return a simple TextView as fallback
            return TextView(requireContext()).apply {
                text = "Error loading UI"
                textSize = 24f
                setPadding(50, 50, 50, 50)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated called")

        try {
            // Restaurar estado si existe
            if (savedInstanceState != null) {
                Log.d(TAG, "Restoring saved state")
                emailText = savedInstanceState.getString("EMAIL_TEXT", "")
                isLoggingIn = savedInstanceState.getBoolean("IS_LOGGING_IN", false)
            }

            // Use SharedPreferences for now to keep it simple
            sharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

            if (sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)) {
                Log.d(TAG, "User already logged in, starting main activity")
                startNavbarActivity()
                return
            }

            // Find views with try/catch to identify which view is causing problems
            try {
                tilEmail = view.findViewById(R.id.tilEmail)
                Log.d(TAG, "Found tilEmail")
            } catch (e: Exception) {
                Log.e(TAG, "Error finding tilEmail: ${e.message}")
            }

            try {
                tilPassword = view.findViewById(R.id.tilPassword)
                Log.d(TAG, "Found tilPassword")
            } catch (e: Exception) {
                Log.e(TAG, "Error finding tilPassword: ${e.message}")
            }

            try {
                etEmail = view.findViewById(R.id.etEmail)
                Log.d(TAG, "Found etEmail")
                // Restaurar texto si existe
                if (emailText.isNotEmpty()) {
                    etEmail.setText(emailText)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error finding etEmail: ${e.message}")
            }

            try {
                etPassword = view.findViewById(R.id.etPassword)
                Log.d(TAG, "Found etPassword")
            } catch (e: Exception) {
                Log.e(TAG, "Error finding etPassword: ${e.message}")
            }

            try {
                btnLogin = view.findViewById(R.id.btnLogin)
                Log.d(TAG, "Found btnLogin")
            } catch (e: Exception) {
                Log.e(TAG, "Error finding btnLogin: ${e.message}")
            }

            try {
                tvRegister = view.findViewById(R.id.tvRegister)
                Log.d(TAG, "Found tvRegister")
            } catch (e: Exception) {
                Log.e(TAG, "Error finding tvRegister: ${e.message}")
            }

            try {
                progressBar = view.findViewById(R.id.progressBar)
                Log.d(TAG, "Found progressBar")
                // Restaurar estado de carga si estaba en proceso
                progressBar.visibility = if (isLoggingIn) View.VISIBLE else View.GONE
            } catch (e: Exception) {
                Log.e(TAG, "Error finding progressBar: ${e.message}")
            }

            btnLogin.setOnClickListener {
                Log.d(TAG, "Login button clicked")
                loginUser()
            }

            tvRegister.setOnClickListener {
                Log.d(TAG, "Register text clicked")
                try {
                    findNavController().navigate(R.id.action_login_to_registro)
                } catch (e: Exception) {
                    Log.e(TAG, "Error navigating to register: ${e.message}", e)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in onViewCreated: ${e.message}", e)
        }
    }

    private fun loginUser() {
        Log.d(TAG, "loginUser called")
        try {
            tilEmail.error = null
            tilPassword.error = null

            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            var isValid = true

            // Guardar email para restaurar después de rotación
            emailText = email

            if (TextUtils.isEmpty(email)) {
                tilEmail.error = getString(R.string.error_field_required)
                isValid = false
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                tilEmail.error = getString(R.string.error_invalid_email)
                isValid = false
            }

            if (TextUtils.isEmpty(password)) {
                tilPassword.error = getString(R.string.error_field_required)
                isValid = false
            } else if (password.length < 6) {
                tilPassword.error = getString(R.string.error_short_password)
                isValid = false
            }

            if (isValid) {
                Log.d(TAG, "Login validation successful")

                // Mostrar progreso
                progressBar.visibility = View.VISIBLE
                isLoggingIn = true

                // Simulación de login (en un caso real, harías la autenticación con Firebase)
                // Usando un handler para simular la demora de red
                android.os.Handler().postDelayed({
                    Log.d(TAG, "Login successful, saving preferences")
                    progressBar.visibility = View.GONE
                    isLoggingIn = false

                    sharedPreferences.edit()
                        .putBoolean(KEY_IS_LOGGED_IN, true)
                        .putString(KEY_EMAIL, email)
                        .apply()
                    startNavbarActivity()
                }, 1000) // Simular 1 segundo de delay para autenticación
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in loginUser: ${e.message}", e)
            isLoggingIn = false
            progressBar.visibility = View.GONE
        }
    }

    private fun startNavbarActivity() {
        Log.d(TAG, "Starting navbar activity")
        try {
            val intent = Intent(requireContext(), Pantallaprincipal::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        } catch (e: Exception) {
            Log.e(TAG, "Error starting navbar activity: ${e.message}", e)
        }
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

        // Guardar estado de la interfaz
        outState.putString("EMAIL_TEXT", emailText)
        outState.putBoolean("IS_LOGGING_IN", isLoggingIn)
    }
}