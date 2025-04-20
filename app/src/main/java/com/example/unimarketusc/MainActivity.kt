package com.example.unimarketusc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Buscar los botones por su ID
        val btnIniciarSesion = findViewById<Button>(R.id.Iniciar_sesion)
        val txtRegistrate = findViewById<TextView>(R.id.Registrate)

        // 2. Cuando se presione "Iniciar sesión"
        btnIniciarSesion.setOnClickListener {
            val intent = Intent(this, inicio_sesion::class.java)
            startActivity(intent)
        }

        // 3. Cuando se presione "Regístrate"
        txtRegistrate.setOnClickListener {
            val intent = Intent(this, registro::class.java)
            startActivity(intent)
        }
    }
}
