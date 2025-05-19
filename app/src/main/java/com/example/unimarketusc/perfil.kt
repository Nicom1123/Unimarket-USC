package com.example.unimarketusc

import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Intent
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import org.json.JSONObject
import java.io.IOException
import com.bumptech.glide.Glide
import com.example.unimarketusc.api.PerfilResponse
import com.example.unimarketusc.api.RetrofitClient
import com.example.unimarketusc.databinding.PerfilBinding
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import java.io.File

class perfil : AppCompatActivity(){

    lateinit var perfilBinding: PerfilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PerfilBinding.inflate(layoutInflater)
        setContentView(R.layout.perfil)

        fun guardarPerfil(userId: Int, imagenUri: Uri?) {
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

            RetrofitClient.instance.guardarPerfil(userIdBody, nombre, direccion, carrera, contacto, filePart)
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        Toast.makeText(this@perfil, "Perfil guardado", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(this@perfil, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }

        fun cargarPerfil(userId: Int) {
            RetrofitClient.instance.obtenerPerfil(userId).enqueue(object : Callback<PerfilResponse> {
                override fun onResponse(call: Call<PerfilResponse>, response: Response<PerfilResponse>) {
                    response.body()?.let {
                        binding.etNombre.setText(it.nombre)
                        binding.etDireccion.setText(it.direccion)
                        binding.etCarrera.setText(it.carrera)
                        binding.etContacto.setText(it.contacto)
                        Glide.with(this@perfil).load(it.imagen).into(binding.imgPerfil)
                    }
                }

                override fun onFailure(call: Call<PerfilResponse>, t: Throwable) {
                    Toast.makeText(this@perfil, "No se pudo cargar el perfil", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }


}