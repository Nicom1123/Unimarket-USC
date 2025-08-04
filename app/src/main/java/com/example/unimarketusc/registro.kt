package com.example.unimarketusc

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.net.URLEncoder

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

            enviarCorreoDeActivacion(email, pwd)
        }
    }

    private fun enviarCorreoDeActivacion(correo: String, clave: String) {
        val queue = Volley.newRequestQueue(this)

        // Codificamos los parámetros para la URL
        val correoCodificado = URLEncoder.encode(correo, "UTF-8")
        val claveCodificada = URLEncoder.encode(clave, "UTF-8")

        val url = "https://rude-lemons-guess.loca.lt/unimarket_usc/enviar_correo.php?correo=$correoCodificado&clave=$claveCodificada"

        val request = StringRequest(
            Request.Method.GET, url,
            { response ->
                if (response.contains("✔️")) {
                    Toast.makeText(this, "Correo enviado. Revisa tu bandeja de entrada", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, inicio_sesion::class.java)) // Redirigir al login
                    finish()
                } else {
                    Toast.makeText(this, "Error del servidor: $response", Toast.LENGTH_LONG).show()
                }
            },
            { error ->
                Toast.makeText(this, "Error al enviar el correo: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(request)
    }
}
