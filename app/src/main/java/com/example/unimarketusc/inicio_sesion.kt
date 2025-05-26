package com.example.unimarketusc

import android.content.Intent
import android.os.Bundle
import android.widget.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import androidx.appcompat.app.AppCompatActivity

class inicio_sesion : AppCompatActivity() {

    private lateinit var etCorreo: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnIniciar: Button
    private lateinit var cbRecordarme: CheckBox
    private lateinit var tvOlvidaste: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inicio_sesion)

        etCorreo = findViewById(R.id.Correo_Institucional)
        etPassword = findViewById(R.id.password)
        btnIniciar = findViewById(R.id.Inicio_Sesionbtn)
        cbRecordarme = findViewById(R.id.remember_me)
        tvOlvidaste = findViewById(R.id.forgot_password)

        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        if (prefs.getBoolean("remember", false)) {
            etCorreo.setText(prefs.getString("email", ""))
            etPassword.setText(prefs.getString("password", ""))
            cbRecordarme.isChecked = true
        }

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }

        btnIniciar.setOnClickListener {
            val email = etCorreo.text.toString().trim()
            val rawPwd = etPassword.text.toString().trim()

            if (email.isEmpty() || rawPwd.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val client = OkHttpClient()
            val form = FormBody.Builder()
                .add("email", email)
                .add("password", rawPwd)  // 👈 YA NO ciframos aquí
                .build()

            val request = Request.Builder()
                .url("http://192.168.56.1/unimarket_usc/login.php")
                .post(form)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        Toast.makeText(this@inicio_sesion, "Error de red", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string()
                    val json = JSONObject(body)

                    runOnUiThread {
                        if (json.getBoolean("success")) {
                            if (cbRecordarme.isChecked) {
                                prefs.edit()
                                    .putBoolean("remember", true)
                                    .putString("email", email)
                                    .putString("password", rawPwd)
                                    .apply()
                            }
                            Toast.makeText(this@inicio_sesion, "Inicio exitoso", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@inicio_sesion, menu::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@inicio_sesion, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }

        tvOlvidaste.setOnClickListener {
            val intent = Intent(this, olvidaste_contrasena::class.java)
            startActivity(intent)
        }
    }
}
