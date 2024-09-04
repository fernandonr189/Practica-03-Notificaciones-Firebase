package com.example.practica03_notificaciones_firebase.models

data class Product(
    val name: String,
    val description: String,
    val imageRes: Int,
    var isInCart: Boolean
)