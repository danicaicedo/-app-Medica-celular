package com.aristidevs.finalmente.menu

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.aristidevs.finalmente.R
import android.content.Intent

class HemorragiaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hemorragia)


        findViewById<LinearLayout>(R.id.homeButtonLayout).setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finish() // Opcional, para cerrar la pantalla actual
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




        val totalPasos = 7
        for (i in 1..totalPasos) {
            val paso = findViewById<LinearLayout>(
                resources.getIdentifier("paso$i", "id", packageName)
            )
            val detalle = findViewById<TextView>(
                resources.getIdentifier("step${i}Detalle", "id", packageName)
            )
            paso?.setOnClickListener {
                if (detalle != null) {
                    detalle.visibility = if (detalle.visibility == View.GONE) View.VISIBLE else View.GONE
                }
            }
        }
    }
}
