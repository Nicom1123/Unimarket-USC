package com.example.unimarketusc

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unimarketusc.model.Conversacion
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class ConversacionesActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private val listaConversaciones = mutableListOf<Conversacion>()
    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversaciones)

        userId = intent.getIntExtra("user_id", 0)

        val btnVolver = findViewById<ImageButton>(R.id.Btnvolver)
        btnVolver.setOnClickListener {
            val intent = Intent(this, menu::class.java)
            intent.putExtra("user_id", userId) // pasa el user_id si lo necesitas
            startActivity(intent)
            finish()
        }

        recycler = findViewById(R.id.recyclerConversaciones)
        recycler.layoutManager = LinearLayoutManager(this)

        obtenerConversaciones()
    }


    private fun obtenerConversaciones() {
        val client = OkHttpClient()
        val url = "https://rude-lemons-guess.loca.lt/unimarket_usc/listar_conversaciones.php?user_id=$userId"

        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@ConversacionesActivity, "Error de red", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val json = JSONArray(body)

                listaConversaciones.clear()
                for (i in 0 until json.length()) {
                    val obj = json.getJSONObject(i)
                    val conversacion = com.example.unimarketusc.model.Conversacion(
                        otro_usuario = obj.getInt("otro_usuario"),
                        nombre_usuario = obj.getString("nombre_usuario"),
                        ultimo_mensaje = obj.getString("ultimo_mensaje"),
                        avatar = obj.optString("avatar", null)
                    )
                    listaConversaciones.add(conversacion)
                }


                runOnUiThread {
                    recycler.adapter = ConversacionAdapter(listaConversaciones, userId)
                }
            }
        })
    }
}
