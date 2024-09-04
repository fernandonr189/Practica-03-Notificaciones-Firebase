package com.example.practica03_notificaciones_firebase.models

import com.example.practica03_notificaciones_firebase.R

object State {

    var products: ArrayList<Product> = arrayListOf()

    fun initialize() {
        products = arrayListOf(Product("PlayStation 5", "Consola de videojuegos de ultima generación", R.drawable.playstation5, false),
            Product("Xbox One", "Consola de videojuegos xbox one", R.drawable.xboxone, false),
            Product("Nintendo Switch", "Consola de videojuegos de nintendo", R.drawable.nintendo_switch, false),
            Product("Nintendo Switch Oled", "Consola de videojuegos nintendo switch con pantalla OLED", R.drawable.nintendo_switch_oled, false))

    }

    fun addToFavorites(position: Int) {
        products.get(position).isInCart = true
    }

    fun removeFromFavorites(position: Int) {
        products.get(position).isInCart = false
    }
}