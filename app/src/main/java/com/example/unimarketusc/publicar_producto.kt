package com.example.unimarketusc

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.unimarketusc.api.RetrofitClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class publicar_producto : AppCompatActivity() {

    private val PICK_IMAGE = 100
    private var imagenUri: Uri? = null
    private val userId = 1 // Simulado

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.publicar_producto)

        val imgProducto = findViewById<ImageView>(R.id.imgProducto)
        val btnSeleccionar = findViewById<Button>(R.id.btnSeleccionarImagen)
        val btnPublicar = findViewById<Button>(R.id.btnPublicar)
        val btnVolver = findViewById<ImageButton>(R.id.btnVolverProducto)

        val etTitulo = findViewById<EditText>(R.id.etTitulo)
        val etDescripcion = findViewById<EditText>(R.id.etDescripcion)
        val etPrecio = findViewById<EditText>(R.id.etPrecio)
        val etCategoria = findViewById<EditText>(R.id.etCategoria)

        val spEstado = findViewById<Spinner>(R.id.spEstado)
        val opcionesEstado = listOf("Nuevo", "Usado")
        val adapterEstado = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionesEstado)
        adapterEstado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spEstado.adapter = adapterEstado

        btnSeleccionar.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE)
        }


        btnVolver.setOnClickListener {
            finish() // ← vuelve a la actividad anterior
        }


        btnPublicar.setOnClickListener {
            val titulo = etTitulo.text.toString()
            val descripcion = etDescripcion.text.toString()
            val precio = etPrecio.text.toString()
            val categoria = etCategoria.text.toString()
            val estado = spEstado.selectedItem.toString()

            if (titulo.isEmpty() || descripcion.isEmpty() || precio.isEmpty() || categoria.isEmpty() || estado.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            subirProducto(userId, titulo, descripcion, precio, categoria, estado, imagenUri)

        }
    }

    private fun subirProducto(
        userId: Int,
        titulo: String,
        descripcion: String,
        precio: String,
        categoria: String,
        estado: String,
        imagenUri: Uri?
    ) {
        val tituloBody = titulo.toRequestBody("text/plain".toMediaTypeOrNull())
        val descripcionBody = descripcion.toRequestBody()
        val precioBody = precio.toRequestBody()
        val categoriaBody = categoria.toRequestBody()
        val userIdBody = userId.toString().toRequestBody()
        val estadoBody = estado.toRequestBody()

        val filePart = imagenUri?.let {
            val file = File(getRealPathFromURI(it))
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("imagen", file.name, requestFile)
        }

        if (filePart == null) {
            Toast.makeText(this, "Debe seleccionar una imagen", Toast.LENGTH_SHORT).show()
            return
        }

        RetrofitClient.productoApi.crearProducto(
            userIdBody, tituloBody, descripcionBody, precioBody, categoriaBody, estadoBody, filePart
        ).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Toast.makeText(this@publicar_producto, "Producto publicado", Toast.LENGTH_SHORT).show()
                finish() // Cierra la pantalla
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@publicar_producto, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getRealPathFromURI(uri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        return cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            it.moveToFirst()
            it.getString(columnIndex)
        } ?: ""
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            imagenUri = data.data
            findViewById<ImageView>(R.id.imgProducto).setImageURI(imagenUri)
        }
    }
}
