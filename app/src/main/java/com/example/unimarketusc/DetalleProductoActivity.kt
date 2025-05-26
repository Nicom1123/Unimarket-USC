package com.example.unimarketusc

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class DetalleProductoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_producto)

        val ivImagen = findViewById<ImageView>(R.id.ivDetalleImagen)
        val tvTitulo = findViewById<TextView>(R.id.tvDetalleTitulo)
        val tvDescripcion = findViewById<TextView>(R.id.tvDetalleDescripcion)
        val tvPrecio = findViewById<TextView>(R.id.tvDetallePrecio)
        val btnChat = findViewById<Button>(R.id.btnHablarVendedor)

        // Obtenemos los datos del producto del intent
        val productoId = intent.getIntExtra("producto_id", 0)
        val titulo = intent.getStringExtra("titulo")
        val descripcion = intent.getStringExtra("descripcion")
        val precio = intent.getDoubleExtra("precio", 0.0)
        val imagen = intent.getStringExtra("imagen")
        val vendedorId = intent.getIntExtra("vendedor_id", 0)
        val userId = intent.getIntExtra("user_id", 0) // ID del usuario actual

        tvTitulo.text = titulo
        tvDescripcion.text = descripcion
        tvPrecio.text = "$$precio"

        Glide.with(this).load(imagen).into(ivImagen)

        btnChat.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("user_id", userId)       // quien escribe
            intent.putExtra("receptor_id", vendedorId) // a quién le escribe
            startActivity(intent)
        }
    }
}
