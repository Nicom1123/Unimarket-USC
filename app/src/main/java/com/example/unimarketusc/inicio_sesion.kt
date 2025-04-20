package com.example.unimarketusc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class inicio_sesion : AppCompatActivity() {
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var cbRemember: CheckBox
    private lateinit var tvForgot: TextView
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inicio_sesion)

        etEmail    = findViewById(R.id.Correo_Institucional)
        etPassword = findViewById(R.id.password)
        cbRemember = findViewById(R.id.remember_me)
        tvForgot   = findViewById(R.id.forgot_password)
        btnLogin   = findViewById(R.id.Inicio_Sesionbtn)

        // Recuperar SharedPreferences
        val prefs = getSharedPreferences("unimarket_prefs", MODE_PRIVATE)
        if (prefs.getBoolean("remember", false)) {
            etEmail.setText(prefs.getString("email",""))
            etPassword.setText(prefs.getString("password",""))
            cbRemember.isChecked = true
        }

        btnLogin.setOnClickListener {
            // Llamada a login.php (verifica en BD unimarket_cuentas)
            // Si success:
            if (cbRemember.isChecked) {
                prefs.edit()
                    .putBoolean("remember", true)
                    .putString("email", etEmail.text.toString())
                    .putString("password", etPassword.text.toString())
                    .apply()
            } else {
                prefs.edit().clear().apply()
            }
            // Navegar a Home...
        }

        tvForgot.setOnClickListener {
            startActivity(Intent(this, olvidaste_contrasena::class.java))
        }
    }
}