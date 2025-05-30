package com.example.unimarketusc

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.IOException

class olvidaste_contrasena : AppCompatActivity() {

    private lateinit var etCorreo: EditText
    private lateinit var btnEnviar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.olvidaste_contrasena)

        etCorreo = findViewById(R.id.Correo_institucional)
        btnEnviar = findViewById(R.id.btnEnviar_correo)

        btnEnviar.setOnClickListener {
            val correo = etCorreo.text.toString().trim()

            if (correo.isEmpty()) {
                Toast.makeText(this, "Por favor ingresa tu correo institucional", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!correo.endsWith("@usc.edu.co")) {
                Toast.makeText(this, "Debe ser un correo @usc.edu.co", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val client = OkHttpClient()
            val form = FormBody.Builder()
                .add("correo", correo)
                .build()

            val request = Request.Builder()
                .url("http://192.168.56.1/unimarket_usc/enviar_restablecer.php") // Asegúrate que exista
                .post(form)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        Toast.makeText(this@olvidaste_contrasena, "Error de red: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    runOnUiThread {
                        Toast.makeText(this@olvidaste_contrasena, "Si el correo está registrado, se enviará un enlace para restablecer la contraseña.", Toast.LENGTH_LONG).show()
                    }
                }
            })
        }

        // Botón de volver
        val btnVolver = findViewById<ImageButton>(R.id.Btnvolver)
        btnVolver.setOnClickListener {
            finish()
        }
    }
}

