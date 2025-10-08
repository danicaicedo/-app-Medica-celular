package com.aristidevs.finalmente.menu

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.aristidevs.finalmente.R

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_memnu)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.btnCPR).setOnClickListener {
            startActivity(Intent(this, RcpActivity::class.java))
        }

        findViewById<Button>(R.id.btnChoking).setOnClickListener {
            startActivity(Intent(this, AtragantamientoActivity::class.java))
        }

        findViewById<Button>(R.id.btnBleeding).setOnClickListener {
            startActivity(Intent(this, HemorragiaActivity::class.java))
        }

        findViewById<Button>(R.id.btnBurns).setOnClickListener {
            startActivity(Intent(this, QuemaduraActivity::class.java))
        }
    }
}
