package com.aristidevs.finalmente.menu

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.aristidevs.finalmente.R
import android.content.Intent


class RcpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rcp)


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




        val paso1 = findViewById<LinearLayout>(R.id.paso1)
        val paso1Detalle = findViewById<TextView>(R.id.step1Detalle)
        paso1.setOnClickListener {
            paso1Detalle.visibility = if (paso1Detalle.visibility == View.GONE) View.VISIBLE else View.GONE
        }

        val paso2 = findViewById<LinearLayout>(R.id.paso2)
        val paso2Detalle = findViewById<TextView>(R.id.step2Detalle)
        paso2.setOnClickListener {
            paso2Detalle.visibility = if (paso2Detalle.visibility == View.GONE) View.VISIBLE else View.GONE
        }

        val paso3 = findViewById<LinearLayout>(R.id.paso3)
        val paso3Detalle = findViewById<TextView>(R.id.step3Detalle)
        paso3.setOnClickListener {
            paso3Detalle.visibility = if (paso3Detalle.visibility == View.GONE) View.VISIBLE else View.GONE
        }

        val paso4 = findViewById<LinearLayout>(R.id.paso4)
        val paso4Detalle = findViewById<TextView>(R.id.step4Detalle)
        paso4.setOnClickListener {
            paso4Detalle.visibility = if (paso4Detalle.visibility == View.GONE) View.VISIBLE else View.GONE
        }

        val paso5 = findViewById<LinearLayout>(R.id.paso5)
        val paso5Detalle = findViewById<TextView>(R.id.step5Detalle)
        paso5.setOnClickListener {
            paso5Detalle.visibility = if (paso5Detalle.visibility == View.GONE) View.VISIBLE else View.GONE
        }
    }
}
