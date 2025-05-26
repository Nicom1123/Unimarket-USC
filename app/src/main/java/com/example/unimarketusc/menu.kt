package com.example.unimarketusc

import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.unimarketusc.adapter.ProductoAdapter
import com.example.unimarketusc.model.Producto
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class menu : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductoAdapter
    private lateinit var listaOriginal: List<Producto>
    private lateinit var buscador: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu)

        val btnCarrito = findViewById<ImageView>(R.id.btnCarrito)
        val btnUsuario = findViewById<ImageView>(R.id.btnUsuario)
        val btnMensajes = findViewById<ImageView>(R.id.btnMensajes)
        val imgCategoriaTecnologia = findViewById<ImageView>(R.id.imgCategoriaTecnologia)
        val imgCategoriaLibreria = findViewById<ImageView>(R.id.imgCategoriaLibreria)
        val imgCategoriaRopa = findViewById<ImageView>(R.id.imgCategoriaRopa)
        val imgCategoriaPapeleria = findViewById<ImageView>(R.id.imgCategoriaPapeleria)

        recyclerView = findViewById(R.id.rvProductos)
        buscador = findViewById(R.id.buscador)

        recyclerView.layoutManager = LinearLayoutManager(this)
        listaOriginal = emptyList()

        cargarProductosDesdeServidor()

        buscador.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                val texto = newText.orEmpty().lowercase()
                val filtrados = listaOriginal.filter {
                    it.titulo.lowercase().contains(texto)
                }
                adapter.actualizar(filtrados)
                return true
            }
        })

        btnCarrito.setOnClickListener {
            val intent = Intent(this, carrito_compras::class.java)
            startActivity(intent)
        }

        btnUsuario.setOnClickListener {
            val intent = Intent(this, perfil::class.java)
            startActivity(intent)
        }

        btnMensajes.setOnClickListener {
            val intent = Intent(this, ConversacionesActivity::class.java)
            startActivity(intent)
        }

        imgCategoriaTecnologia.setOnClickListener {
            filtrarPorCategoria("tecnologia")
        }

        imgCategoriaLibreria.setOnClickListener {
            filtrarPorCategoria("libreria")
        }

        imgCategoriaRopa.setOnClickListener {
            filtrarPorCategoria("ropa")
        }

        imgCategoriaPapeleria.setOnClickListener {
            filtrarPorCategoria("papeleria")
        }

    }
    private fun cargarProductosDesdeServidor() {
        val url = "http://192.168.56.1/unimarket_usc/listar_todos.php"
        val queue = Volley.newRequestQueue(this)

        val request = JsonArrayRequest(Request.Method.GET, url, null, { response ->
            val lista = mutableListOf<Producto>()
            for (i in 0 until response.length()) {
                val obj = response.getJSONObject(i)
                val producto = Producto(
                    id = obj.getInt("id"),
                    user_id = obj.getInt("user_id"),
                    titulo = obj.getString("titulo"),
                    descripcion = obj.getString("descripcion"),
                    precio = obj.getDouble("precio"),
                    imagen = obj.getString("imagen") ,
                    categoria = obj.getString("categoria")
                )
                lista.add(producto)
            }

            listaOriginal = lista
            adapter = ProductoAdapter(lista)
            recyclerView.adapter = adapter

        }, {
            Toast.makeText(this, "Error al obtener productos", Toast.LENGTH_SHORT).show()
        })

        queue.add(request)
    }
    private fun filtrarPorCategoria(nombreCategoria: String) {
        val filtrados = listaOriginal.filter {
            it.categoria.equals(nombreCategoria, ignoreCase = true)
        }
        adapter.actualizar(filtrados)
    }

}
