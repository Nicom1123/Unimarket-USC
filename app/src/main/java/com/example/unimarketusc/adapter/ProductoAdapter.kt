package com.example.unimarketusc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.unimarketusc.R
import com.example.unimarketusc.model.Producto

class ProductoAdapter(private var productos: List<Producto>) :
    RecyclerView.Adapter<ProductoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titulo: TextView = view.findViewById(R.id.tvTitulo)
        val descripcion: TextView = view.findViewById(R.id.tvDescripcion)
        val precio: TextView = view.findViewById(R.id.tvPrecio)
        val imagen: ImageView = view.findViewById(R.id.ivProducto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val producto = productos[position]
        holder.titulo.text = producto.titulo
        holder.descripcion.text = producto.descripcion
        holder.precio.text = "$${producto.precio}"
        Glide.with(holder.itemView.context).load(producto.imagen).into(holder.imagen)
    }

    override fun getItemCount() = productos.size

    fun actualizar(listaFiltrada: List<Producto>) {
        productos = listaFiltrada
        notifyDataSetChanged()
    }
}
