package com.example.unimarketusc

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class categoria_papeleria : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.categoria_papeleria)
        val btnvolver = findViewById<ImageButton>(R.id.Btnvolver)
        btnvolver.setOnClickListener {
            finish()
        }
    }
}