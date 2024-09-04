package com.example.practica03_notificaciones_firebase.activities

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.practica03_notificaciones_firebase.R
import com.example.practica03_notificaciones_firebase.models.Notifications
import com.example.practica03_notificaciones_firebase.models.PopUpMenu
import com.example.practica03_notificaciones_firebase.models.Product
import com.example.practica03_notificaciones_firebase.models.ProductsRecyclerAdapter
import com.example.practica03_notificaciones_firebase.models.State

class ShoppingCart : AppCompatActivity(), PopUpMenu {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataset: ArrayList<Product>
    private lateinit var productsRecyclerAdapter: ProductsRecyclerAdapter
    private var state = State
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_shopping_cart)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initializeActionBar()

        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    Toast.makeText(
                        this@ShoppingCart,
                        "Verás notificaciones :)",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    Toast.makeText(
                        this@ShoppingCart,
                        "No verás notificaciones :(",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        dataset = state.products.filter {
            it.isInCart
        } as ArrayList<Product>

        productsRecyclerAdapter = ProductsRecyclerAdapter(dataset, this)

        recyclerView = findViewById(R.id.cart_recycler)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = productsRecyclerAdapter
    }

    private fun initializeActionBar() {
        val toolbar = findViewById<Toolbar>(R.id.cart_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Carrito de compras"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        val color = ContextCompat.getColor(this, R.color.md_theme_primaryContainer)
        window.statusBarColor = color
    }


    override fun showPopupMenu(view: View, position: Int) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.menuInflater.inflate(R.menu.popup_menu_cart, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.remove_from_cart -> {
                    Toast.makeText(this, "Eliminado ${dataset[position].name} del carrito", Toast.LENGTH_SHORT).show()
                    Notifications.showBasicNotification("Eliminado ${dataset[position].name} del carrito", "Puedes volver a añadirlo en la pantalla de productos", this, requestPermissionLauncher)

                    val selectedProduct = dataset[position]

                    state.products.first {
                        it == selectedProduct
                    }.apply {
                        isInCart = false
                    }
                    dataset.removeAt(position)
                    if(dataset.isEmpty()) {
                        finish()
                    }
                    productsRecyclerAdapter.notifyItemRemoved(position)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }
}