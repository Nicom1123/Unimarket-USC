package com.example.unimarketusc.model

import java.io.Serializable

data class Producto(
    val id: Int,
    val user_id: Int,
    val titulo: String,
    val descripcion: String,
    val precio: Double,
    val imagen: String
) : Serializable