package com.example.unimarketusc

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class carrito_compras : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.carrito_compras)

        val btnVolver = findViewById<ImageButton>(R.id.Btnvolver)

        btnVolver.setOnClickListener {
            finish()
        }
    }
}

