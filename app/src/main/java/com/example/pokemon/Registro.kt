package com.example.pokemon

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pokemon.databinding.FragmentRegistroBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Registro : Fragment() {

    private var _binding: FragmentRegistroBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegistroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirm = binding.etConfirmPassword.text.toString().trim()

            // Validaciones
            if (username.isEmpty()) {
                binding.tilUsername.error = "Requerido"
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                binding.tilEmail.error = "Requerido"
                return@setOnClickListener
            }
            if (password.length < 6) {
                binding.tilPassword.error = "Mínimo 6 caracteres"
                return@setOnClickListener
            }
            if (password != confirm) {
                binding.tilConfirmPassword.error = "No coinciden"
                return@setOnClickListener
            }

            binding.progressBar.visibility = View.VISIBLE

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    binding.progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                        val data = hashMapOf("username" to username, "email" to email)
                        firestore.collection("users").document(userId).set(data)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                                findNavController().navigate(R.id.action_registro_to_login)
                            }
                    } else {
                        Toast.makeText(context, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        binding.tvLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registro_to_login)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
