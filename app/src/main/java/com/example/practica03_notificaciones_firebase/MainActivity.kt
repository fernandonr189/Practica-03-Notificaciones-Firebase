package com.example.practica03_notificaciones_firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.practica03_notificaciones_firebase.activities.Products
import com.example.practica03_notificaciones_firebase.models.Notifications
import com.example.practica03_notificaciones_firebase.models.State
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var exitButton: Button
    private var loginAttempts: Int = 0
    private var state: State = State

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        createNotificationChannel()
        getFirebaseToken()

        state.initialize()

        usernameEditText = findViewById(R.id.userEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        exitButton = findViewById(R.id.exitButton)
        
        exitButton.setOnClickListener { 
            finish()
        }
        
        loginButton.setOnClickListener { 
            if(validateForm()) {
                val intent: Intent = Intent(this, Products::class.java).apply {
                    putExtra("username", usernameEditText.text.toString())
                }
                startActivity(intent)
            }
            else {
                loginAttempts++
            }
        }
    }

    private fun getFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            //val msg = getString(R.string.msg_token_fmt, token)
            Log.d(TAG, "token: $token")
            //Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })
    }
    
    
    private fun validateForm(): Boolean {
        if(loginAttempts >= 3) {
            Toast.makeText(this, "Limite de intentos alcanzado", Toast.LENGTH_SHORT).show()
            Notifications.showBasicNotification("Usuario bloqueado", "Su usuario ha sido bloqueado, intentelo de nuevo mas tarde", this, requestPermissionLauncher)
            return false
        }
        if(usernameEditText.text.isNullOrEmpty()) {
            Toast.makeText(this, "Ingrese un usuario valido", Toast.LENGTH_SHORT).show()
            return false
        }
        if(passwordEditText.text.isNullOrEmpty()) {
            Toast.makeText(this, "Ingrese una contraseña", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun createNotificationChannel() {

        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    Toast.makeText(
                        this@MainActivity,
                        "Verás notificaciones :)",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "No verás notificaciones :(",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "BasicNotification"
            val descriptionText = "Notificaciones basicas"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("BASIC_CHANNEL", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
