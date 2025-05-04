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
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pokemon.databinding.LoginBinding
import com.google.firebase.auth.FirebaseAuth

class Login : Fragment() {

    private lateinit var binding: LoginBinding
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
    ): View {
        Log.d(TAG, "onCreateView called")
        binding = LoginBinding.inflate(inflater, container, false)
        return binding.root
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

            binding.btnLogin.setOnClickListener {
                Log.d(TAG, "Login button clicked")
                loginUser()
            }

            binding.tvRegister.setOnClickListener {
                Log.d(TAG, "Register text clicked")
                try {
                    // Use Navigation Component to navigate to the registration screen
                    findNavController().navigate(R.id.action_login_to_registro)
                } catch (e: Exception) {
                    Log.e(TAG, "Error navigating to register: ${e.message}", e)
                    // As fallback, use the old method if navigation fails
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_main, Registro())
                        .addToBackStack(null)
                        .commit()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in onViewCreated: ${e.message}", e)
        }
    }

    private fun loginUser() {
        Log.d(TAG, "loginUser called")
        try {
            binding.tilEmail.error = null
            binding.tilPassword.error = null

            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            var isValid = true

            if (TextUtils.isEmpty(email)) {
                binding.tilEmail.error = getString(R.string.error_field_required)
                isValid = false
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.tilEmail.error = getString(R.string.error_invalid_email)
                isValid = false
            }

            if (TextUtils.isEmpty(password)) {
                binding.tilPassword.error = getString(R.string.error_field_required)
                isValid = false
            } else if (password.length < 6) {
                binding.tilPassword.error = getString(R.string.error_short_password)
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