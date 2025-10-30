package com.aristidevs.finalmente.menu

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.aristidevs.finalmente.R

class MenuActivity : AppCompatActivity() {

    private val REQUEST_CALL_PERMISSION = 1
    private val emergencyNumber = "123"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_memnu)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        findViewById<LinearLayout>(R.id.cardSearch).setOnClickListener {
            startActivity(Intent(this, BusquedaActivity::class.java))
        }
        findViewById<LinearLayout>(R.id.profileButtonLayout).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        findViewById<LinearLayout>(R.id.MapaButtonLayout).setOnClickListener {
            // Aquí puedes iniciar la actividad deseada, por ejemplo:
            startActivity(Intent(this, MapaActivity::class.java))
        }



        findViewById<LinearLayout>(R.id.cardCPR).setOnClickListener {
            startActivity(Intent(this, RcpActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.cardChoking).setOnClickListener {
            startActivity(Intent(this, AtragantamientoActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.cardBleeding).setOnClickListener {
            startActivity(Intent(this, HemorragiaActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.cardBurns).setOnClickListener {
            startActivity(Intent(this, QuemaduraActivity::class.java))
        }

        val btnEmergencyCall = findViewById<Button>(R.id.btnEmergencyCall)
        btnEmergencyCall.setOnClickListener {
            Toast.makeText(this, "Llamada de emergencia iniciada", Toast.LENGTH_SHORT).show()
            makeEmergencyCall()
        }
    }

    private fun makeEmergencyCall() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CALL_PERMISSION)
        } else {
            startCall()
        }
    }

    private fun startCall() {
        val dial = "tel:$emergencyNumber"
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse(dial)
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCall()
            } else {
                Toast.makeText(this, "Permiso de llamada denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
