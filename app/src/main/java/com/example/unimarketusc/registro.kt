package com.example.unimarketusc

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

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

        // 1. Bind views
        etName        = findViewById(R.id.etName)
        etDoc         = findViewById(R.id.etDoc)
        etEmail       = findViewById(R.id.etEmail)
        etPassword    = findViewById(R.id.etPassword)
        etConfirmPwd  = findViewById(R.id.etConfirmPwd)
        cbVerifyUSC   = findViewById(R.id.cbVerifyUSC)
        cbTerms       = findViewById(R.id.cbTerms)
        btnRegister   = findViewById(R.id.btnRegister)
        btnBack       = findViewById(R.id.btnBack)

        // 2. Botón "volver"
        btnBack.setOnClickListener { finish() }

        // 3. Validación y registro
        btnRegister.setOnClickListener {
            // 3.1 Campos obligatorios
            if (etName.text.isBlank() || etDoc.text.isBlank() ||
                etEmail.text.isBlank() || etPassword.text.isBlank() ||
                etConfirmPwd.text.isBlank()) {
                Toast.makeText(this, "Faltan datos por llenar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 3.2 Checkboxes obligatorias
            if (!cbVerifyUSC.isChecked || !cbTerms.isChecked) {
                Toast.makeText(this, "Debe aceptar ambas casillas", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 3.3 Correo institucional
            val email = etEmail.text.toString().trim()
            if (!email.endsWith("@usc.edu.co")) {
                etEmail.error = "Debe usar un correo @usc.edu.co"
                return@setOnClickListener
            }

            // 3.4 Contraseñas iguales
            val pwd  = etPassword.text.toString()
            val pwd2 = etConfirmPwd.text.toString()
            if (pwd != pwd2) {
                etConfirmPwd.error = "Las contraseñas no coinciden"
                return@setOnClickListener
            }

            // 3.5 Aquí llamas a tu verificación en MySQL vía PHP/Retrofit...
            verificarEstudianteYRegistrar(
                nombre = etName.text.toString(),
                documento = etDoc.text.toString(),
                email = email,
                password = pwd
            )
        }
    }

    private fun verificarEstudianteYRegistrar(nombre: String, documento: String, email: String, password: String) {
        // EJEMPLO con Retrofit (simplificado):
        // 1) Llamada a verify_student.php que recibe documento+email → devuelve success: true/false
        // 2) Si true, llamas a register.php que inserta en la otra BD → devuelve success
        // 3) Si todo ok, lanzas Intent a InicioSesionActivity

        // ... implementación de tu cliente HTTP aquí ...
    }
}
