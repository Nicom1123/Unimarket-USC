package com.example.unimarketusc.model

data class Conversacion(
    val otro_usuario: Int,
    val nombre_usuario: String,
    val ultimo_mensaje: String,
    val avatar: String? = null
)

