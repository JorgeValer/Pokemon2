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
    private lateinit var sharedPreferences: SharedPreferences

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

            btnLogin.setOnClickListener {
                Log.d(TAG, "Login button clicked")
                loginUser()
            }

            tvRegister.setOnClickListener {
                Log.d(TAG, "Register text clicked")
                try {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_main, Registro())
                        .addToBackStack(null)
                        .commit()
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
                Log.d(TAG, "Login validation successful, saving preferences")
                // For testing, just use SharedPreferences instead of Firebase
                sharedPreferences.edit()
                    .putBoolean(KEY_IS_LOGGED_IN, true)
                    .putString(KEY_EMAIL, email)
                    .apply()
                startNavbarActivity()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in loginUser: ${e.message}", e)
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
}