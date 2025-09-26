package com.example.distribuidoraali

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.distribuidoraali.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import androidx.appcompat.app.AppCompatDelegate
import android.widget.ImageButton


class MainActivity : AppCompatActivity() {

    // aca Se usa lateinit para inicializar las variables más tarde
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // se Infla el layout usando View Binding para mejorar
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupThemeToggle() // Llama a la función Dark o Light Mode

        // se Inicializa la instancia de Firebase Authentication
        auth = FirebaseAuth.getInstance()

        //aca se Configura los clics de los botones
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Log.d("FirebaseAuth", "signInWithEmail:success")
                            updateUI(auth.currentUser)
                        } else {
                            Log.w("FirebaseAuth", "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext,
                                "Autenticación fallida. Revisa tus credenciales.",
                                Toast.LENGTH_SHORT
                            ).show()
                            updateUI(null)
                        }
                    }
            } else {
                Toast.makeText(this, "Por favor, ingresa correo y contraseña.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.registerButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Log.d("FirebaseAuth", "createUserWithEmail:success")
                            Toast.makeText(baseContext, "Registro exitoso.", Toast.LENGTH_SHORT).show()
                            updateUI(auth.currentUser)
                        } else {
                            Log.w("FirebaseAuth", "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext,
                                "Error en el registro: " + task.exception?.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            updateUI(null)
                        }
                    }
            } else {
                Toast.makeText(this, "Por favor, ingresa correo y contraseña.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // en  Esta función se llama al inicio de la actividad para verificar si hay un usuario logueado
    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    // se Navega a la MenuActivity si el usuario existe
    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            Toast.makeText(this, "Sesión iniciada con: ${user.email}", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setupThemeToggle() {
        val themeToggleButton = findViewById<ImageButton>(R.id.themeToggleButton)

        themeToggleButton.setOnClickListener {
            // Aca se verifica  el tema actual y cambia al opuesto
            val isNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

            if (isNightMode) {
                // Si está en modo oscuro, cambia a modo claro
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                // Si está en modo claro, cambia a modo oscuro
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }
}
