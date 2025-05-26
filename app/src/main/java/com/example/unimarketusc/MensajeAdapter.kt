package com.example.unimarketusc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MensajeAdapter(private val listaMensajes: List<Mensaje>, private val userId: Int) :
    RecyclerView.Adapter<MensajeAdapter.MensajeViewHolder>() {

    inner class MensajeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtMensaje: TextView = view.findViewById(R.id.txtMensaje)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MensajeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mensaje, parent, false)
        return MensajeViewHolder(view)
    }

    override fun onBindViewHolder(holder: MensajeViewHolder, position: Int) {
        val mensaje = listaMensajes[position]
        holder.txtMensaje.text = mensaje.mensaje

        // Cambiar el fondo según sea emisor o receptor
        if (mensaje.emisor_id == userId) {
            holder.txtMensaje.setBackgroundResource(R.drawable.bg_mensaje_enviado)
        } else {
            holder.txtMensaje.setBackgroundResource(R.drawable.bg_mensaje_recibido)
        }
    }

    override fun getItemCount(): Int = listaMensajes.size
}

