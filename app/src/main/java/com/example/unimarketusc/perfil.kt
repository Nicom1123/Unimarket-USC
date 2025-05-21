package com.example.unimarketusc

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.unimarketusc.api.PerfilResponse
import com.example.unimarketusc.api.RetrofitClient
import com.example.unimarketusc.databinding.PerfilBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class perfil : AppCompatActivity() {

    private lateinit var binding: PerfilBinding
    private var imagenUri: Uri? = null
    private val PICK_IMAGE = 100
    private val userId = 1 // ← Simulamos el usuario con ID 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Botón volver
        val btnVolver = findViewById<ImageButton>(R.id.Btnvolver)
        btnVolver.setOnClickListener {
            startActivity(Intent(this, menu::class.java))
            finish()
        }

        // Botón cambiar imagen
        binding.btnCambiarImagen.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE)
        }

        // Botón guardar perfil
        binding.btnGuardar.setOnClickListener {
            guardarPerfil(userId, imagenUri)
        }

        // Botón cerrar sesión
        binding.btnCerrarSesion.setOnClickListener {
            startActivity(Intent(this, inicio_sesion::class.java))
            finish()
        }

        // Cargar datos al abrir
        cargarPerfil(userId)
    }

    private fun guardarPerfil(userId: Int, imagenUri: Uri?) {
        val filePart = imagenUri?.let {
            val file = File(getRealPathFromURI(it))
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("imagen", file.name, requestFile)
        }

        val nombre = binding.etNombre.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val direccion = binding.etDireccion.text.toString().toRequestBody()
        val carrera = binding.etCarrera.text.toString().toRequestBody()
        val contacto = binding.etContacto.text.toString().toRequestBody()
        val userIdBody = userId.toString().toRequestBody()

        RetrofitClient.instance.guardarPerfil(
            userIdBody,
            nombre,
            direccion,
            carrera,
            contacto,
            filePart
        ).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Toast.makeText(this@perfil, "Perfil guardado", Toast.LENGTH_SHORT).show()

                // 🔄 Volver a cargar los datos en pantalla
                cargarPerfil(userId)
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@perfil, "Error al guardar: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun cargarPerfil(userId: Int) {
        RetrofitClient.instance.obtenerPerfil(userId)
            .enqueue(object : Callback<PerfilResponse> {
                override fun onResponse(call: Call<PerfilResponse>, response: Response<PerfilResponse>) {
                    val perfil = response.body()
                    if (perfil != null) {
                        binding.etNombre.setText(perfil.nombre)
                        binding.etDireccion.setText(perfil.direccion)
                        binding.etCarrera.setText(perfil.carrera)
                        binding.etContacto.setText(perfil.contacto)
                        Glide.with(this@perfil).load(perfil.imagen).into(binding.imgPerfil)
                    } else {
                        Toast.makeText(this@perfil, "No se encontró el perfil", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<PerfilResponse>, t: Throwable) {
                    Toast.makeText(this@perfil, "Error al cargar perfil", Toast.LENGTH_SHORT).show()
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
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imagenUri = data.data
            binding.imgPerfil.setImageURI(imagenUri)
        }
    }
}
