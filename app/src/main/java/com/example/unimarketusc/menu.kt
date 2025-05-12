package com.example.unimarketusc

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu)

        val btnCarrito = findViewById<ImageView>(R.id.btnCarrito)
        val btnUsuario = findViewById<ImageView>(R.id.btnUsuario)
        val imgCategoriaTecnologia = findViewById<ImageView>(R.id.imgCategoriaTecnologia)
        val imgCategoriaLibreria = findViewById<ImageView>(R.id.imgCategoriaLibreria)

        btnCarrito.setOnClickListener {
            val intent = Intent(this, carrito_compras::class.java)
            startActivity(intent)
        }

        btnUsuario.setOnClickListener {
            val intent = Intent(this, usuario::class.java)
            startActivity(intent)
        }


        imgCategoriaTecnologia.setOnClickListener {
            val intent = Intent(this, categoria_tecnologia::class.java)
            startActivity(intent)
        }

        imgCategoriaLibreria.setOnClickListener {
            val intent = Intent(this, categoria_libreria::class.java)
            startActivity(intent)
        }

    }
}
