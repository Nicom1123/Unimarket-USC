package com.example.unimarketusc

import android.content.Intent
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.unimarketusc.model.Conversacion

class ConversacionAdapter(
    private val lista: List<Conversacion>,
    private val userId: Int
) : RecyclerView.Adapter<ConversacionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombre)
        val tvMensaje: TextView = view.findViewById(R.id.tvUltimoMensaje)
        val ivAvatar: ImageView = view.findViewById(R.id.ivAvatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_conversacion, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val conversacion = lista[position]

        holder.tvNombre.text = conversacion.nombre_usuario
        holder.tvMensaje.text = conversacion.ultimo_mensaje

        // Cargar avatar si hay, o imagen por defecto
        Glide.with(holder.itemView.context)
            .load(conversacion.avatar ?: "")
            .placeholder(R.drawable.perfil_usuario)
            .into(holder.ivAvatar)

    }

    override fun getItemCount() = lista.size
}
