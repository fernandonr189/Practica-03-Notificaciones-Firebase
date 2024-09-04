package com.example.practica03_notificaciones_firebase.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.practica03_notificaciones_firebase.R

class ProductsRecyclerAdapter(private val dataset: ArrayList<Product>, private val popupMenu: PopUpMenu): RecyclerView.Adapter<ProductsRecyclerAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val productTitle: TextView
        val productDescription: TextView
        val productImage: ImageView

        init {
            productTitle = view.findViewById(R.id.product_name)
            productDescription = view.findViewById(R.id.product_description)
            productImage = view.findViewById(R.id.product_image)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductsRecyclerAdapter.ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_card_element, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductsRecyclerAdapter.ViewHolder, position: Int) {
        holder.productTitle.text = dataset[position].name
        holder.productDescription.text = dataset[position].description
        holder.productImage.setImageResource(dataset[position].imageRes)

        holder.itemView.setOnLongClickListener {
            popupMenu.showPopupMenu(holder.itemView, position)
            true
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }


}