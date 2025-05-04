package com.example.pokemon

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pokemon.databinding.RegistroBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Registro : Fragment() {

    private lateinit var binding: RegistroBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    companion object {
        const val TAG = "RegistroFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RegistroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated called")

        try {
            // Initialize Firebase Auth and Firestore
            auth = FirebaseAuth.getInstance()
            firestore = FirebaseFirestore.getInstance()

            binding.progressBar.visibility = View.GONE

            binding.btnRegister.setOnClickListener {
                registerUser()
            }

            binding.tvLogin.setOnClickListener {
                try {
                    // Use Navigation Component to navigate back to login
                    findNavController().navigate(R.id.action_registro_to_login)
                } catch (e: Exception) {
                    Log.e(TAG, "Error navigating to login: ${e.message}", e)
                    // Fallback to fragment transaction if navigation fails
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_main, Login())
                        .commit()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in onViewCreated: ${e.message}", e)
        }
    }

    private fun registerUser() {
        binding.tilUsername.error = null
        binding.tilEmail.error = null
        binding.tilPassword.error = null
        binding.tilConfirmPassword.error = null

        val username = binding.etUsername.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()

        var isValid = true

        if (TextUtils.isEmpty(username)) {
            binding.tilUsername.error = getString(R.string.error_field_required)
            isValid = false
        }

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

        if (TextUtils.isEmpty(confirmPassword)) {
            binding.tilConfirmPassword.error = getString(R.string.error_field_required)
            isValid = false
        } else if (password != confirmPassword) {
            binding.tilConfirmPassword.error = getString(R.string.error_passwords_not_match)
            isValid = false
        }

        if (isValid) {
            binding.progressBar.visibility = View.VISIBLE

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Save additional user information in Firestore
                        val userId = auth.currentUser?.uid
                        val userMap = hashMapOf(
                            "username" to username,
                            "email" to email
                        )

                        if (userId != null) {
                            firestore.collection("users").document(userId)
                                .set(userMap)
                                .addOnSuccessListener {
                                    binding.progressBar.visibility = View.GONE
                                    Toast.makeText(requireContext(), R.string.registration_successful, Toast.LENGTH_SHORT).show()
                                    startNavbar()
                                }
                                .addOnFailureListener { e ->
                                    binding.progressBar.visibility = View.GONE
                                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun startNavbar() {
        val intent = Intent(requireContext(), Pantallaprincipal::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }
}