package com.example.unimarketusc.model

import java.io.Serializable

data class Producto(
    val id: Int,
    val user_id: Int,  // This should match exactly with your API response
    val titulo: String,
    val descripcion: String,
    val precio: Double,
    val imagen: String,
    val categoria: String
) : Serializable