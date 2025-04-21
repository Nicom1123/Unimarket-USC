package com.example.unimarketusc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONObject
import okhttp3.*
import java.io.IOException

class registro : AppCompatActivity() {
    private lateinit var etName: EditText
    private lateinit var etDoc: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPwd: EditText
    private lateinit var cbVerifyUSC: CheckBox
    private lateinit var cbTerms: CheckBox
    private lateinit var btnRegister: Button
    private lateinit var btnBack: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro)

        // Vinculación de vistas
        etName        = findViewById(R.id.etName)
        etDoc         = findViewById(R.id.etDoc)
        etEmail       = findViewById(R.id.etEmail)
        etPassword    = findViewById(R.id.etPassword)
        etConfirmPwd  = findViewById(R.id.etConfirmPwd)
        cbVerifyUSC   = findViewById(R.id.cbVerifyUSC)
        cbTerms       = findViewById(R.id.cbTerms)
        btnRegister   = findViewById(R.id.btnRegister)
        btnBack       = findViewById(R.id.btnBack)

        btnBack.setOnClickListener { finish() }

        btnRegister.setOnClickListener {
            val name = etName.text.toString().trim()
            val doc = etDoc.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val pwd = etPassword.text.toString()
            val confirmPwd = etConfirmPwd.text.toString()

            // Validaciones
            if (name.isEmpty() || doc.isEmpty() || email.isEmpty() || pwd.isEmpty() || confirmPwd.isEmpty()) {
                Toast.makeText(this, "Faltan datos por llenar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!cbVerifyUSC.isChecked || !cbTerms.isChecked) {
                Toast.makeText(this, "Debe aceptar ambas casillas", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!email.endsWith("@usc.edu.co")) {
                etEmail.error = "Debe usar un correo institucional"
                return@setOnClickListener
            }

            if (pwd != confirmPwd) {
                etConfirmPwd.error = "Las contraseñas no coinciden"
                return@setOnClickListener
            }

            // Llamar verificación y registro
            verificarEstudianteYRegistrar(doc, email, pwd)
        }
    }

    private fun verificarEstudianteYRegistrar(documento: String, email: String, password: String) {
        val client = OkHttpClient()

        val formVerify = FormBody.Builder()
            .add("documento", documento)
            .add("email", email)
            .build()

        val requestVerify = Request.Builder()
            .url("http://10.0.2.2/unimarket_usc/verify_student.php")
            .post(formVerify)
            .build()

        client.newCall(requestVerify).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@registro, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val res = response.body?.string()
                val json = JSONObject(res ?: "{}")

                if (json.getBoolean("success")) {
                    // Verificado en la BD de estudiantes, ahora registrar
                    val formRegister = FormBody.Builder()
                        .add("documento", documento)
                        .add("email", email)
                        .add("password", password)
                        .build()

                    val requestRegister = Request.Builder()
                        .url("http://10.0.2.2/unimarket_usc/register.php")
                        .post(formRegister)
                        .build()

                    client.newCall(requestRegister).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            runOnUiThread {
                                Toast.makeText(this@registro, "Error al registrar", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onResponse(call: Call, response: Response) {
                            val jsonReg = JSONObject(response.body?.string() ?: "{}")
                            runOnUiThread {
                                if (jsonReg.getBoolean("success")) {
                                    Toast.makeText(this@registro, "Registro exitoso", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this@registro, inicio_sesion::class.java))
                                    finish()
                                } else {
                                    Toast.makeText(this@registro, "Error al guardar", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    })

                } else {
                    runOnUiThread {
                        Toast.makeText(this@registro, "Usuario no encontrado en base de datos USC", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}