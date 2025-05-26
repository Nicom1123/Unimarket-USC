package com.example.unimarketusc

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class ChatActivity : AppCompatActivity() {

    private lateinit var recyclerMensajes: RecyclerView
    private lateinit var etMensaje: EditText
    private lateinit var btnEnviar: Button

    private var userId = 0       // El ID del usuario actual
    private var receptorId = 0   // El ID del usuario con quien se habla

    private val listaMensajes = mutableListOf<Mensaje>()
    private lateinit var adaptador: MensajeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        recyclerMensajes = findViewById(R.id.recyclerMensajes)
        etMensaje = findViewById(R.id.etMensaje)
        btnEnviar = findViewById(R.id.btnEnviar)

        // 🔐 Aquí debes obtener el user_id y receptor_id desde la actividad anterior
        userId = intent.getIntExtra("user_id", 0)
        receptorId = intent.getIntExtra("receptor_id", 0)

        // Configurar RecyclerView
        recyclerMensajes.layoutManager = LinearLayoutManager(this)
        adaptador = MensajeAdapter(listaMensajes, userId)
        recyclerMensajes.adapter = adaptador

        // Cargar mensajes al inicio
        cargarMensajes()

        btnEnviar.setOnClickListener {
            val texto = etMensaje.text.toString().trim()
            if (texto.isNotEmpty()) {
                enviarMensaje(texto)
            }
        }
    }

    private fun cargarMensajes() {
        val client = OkHttpClient()
        val url = "http://192.168.56.1/unimarket_usc/obtener_mensajes.php?emisor_id=$userId&receptor_id=$receptorId"

        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@ChatActivity, "Error al cargar mensajes", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val jsonArray = JSONArray(body)
                listaMensajes.clear()
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    val mensaje = Mensaje(
                        emisor_id = obj.getInt("emisor_id"),
                        receptor_id = obj.getInt("receptor_id"),
                        mensaje = obj.getString("mensaje"),
                        fecha = obj.getString("fecha")
                    )
                    listaMensajes.add(mensaje)
                }

                runOnUiThread {
                    adaptador.notifyDataSetChanged()
                    recyclerMensajes.scrollToPosition(listaMensajes.size - 1)
                }
            }
        })
    }

    private fun enviarMensaje(texto: String) {
        val client = OkHttpClient()
        val form = FormBody.Builder()
            .add("emisor_id", userId.toString())
            .add("receptor_id", receptorId.toString())
            .add("mensaje", texto)
            .build()

        val request = Request.Builder()
            .url("http://192.168.56.1/unimarket_usc/enviar_mensaje.php")
            .post(form)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@ChatActivity, "Error al enviar mensaje", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    etMensaje.text.clear()
                    cargarMensajes()  // Actualiza la conversación
                }
            }
        })
    }
}
