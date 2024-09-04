package com.example.practica03_notificaciones_firebase.activities

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import com.example.practica03_notificaciones_firebase.models.Alarms
import com.example.practica03_notificaciones_firebase.models.Notifications
import com.example.practica03_notificaciones_firebase.models.PopUpMenu
import com.example.practica03_notificaciones_firebase.models.Product
import com.example.practica03_notificaciones_firebase.models.ProductsRecyclerAdapter
import com.example.practica03_notificaciones_firebase.models.State

class Products : AppCompatActivity(), PopUpMenu{

    private var username: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var dataset: ArrayList<Product>
    private var state: State = State
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_products)
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
                        this@Products,
                        "Verás notificaciones :)",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    Toast.makeText(
                        this@Products,
                        "No verás notificaciones :(",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        username = intent.getStringExtra("username")

        dataset = state.products

        val productsRecyclerAdapter = ProductsRecyclerAdapter(dataset, this)

        recyclerView = findViewById(R.id.products_recycler)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = productsRecyclerAdapter

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.go_to_cart -> {
                val intent = Intent(this, ShoppingCart::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.products_actionbar_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    private fun initializeActionBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Productos"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        val color = ContextCompat.getColor(this, R.color.md_theme_primaryContainer)
        window.statusBarColor = color
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }


    override fun showPopupMenu(view: View, position: Int) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.add_to_cart -> {
                    Toast.makeText(this, "Añadido ${dataset[position].name} al carrito", Toast.LENGTH_SHORT).show()
                    Notifications.showBasicNotification("Añadido ${dataset[position].name} al carrito","Puedes eliminarlo en la pantalla de Carrito", this, requestPermissionLauncher)
                    dataset[position].isInCart = true
                    state.products[position] = dataset[position]
                    true
                }
                R.id.discount_notification -> {
                    Toast.makeText(this, "Configurada alarma para ${dataset[position].name}", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, AlarmActivity::class.java)
                    val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

                    Alarms.repetitiveAlarm(pendingIntent, this)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }
}