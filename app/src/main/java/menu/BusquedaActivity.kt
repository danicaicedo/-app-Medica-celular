package com.aristidevs.finalmente.menu

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.aristidevs.finalmente.R

data class Emergencia(
    val nombre: String,
    val descripcion: String,
    val destino: Class<*>
)

class BusquedaActivity : AppCompatActivity() {

    private val listaEmergencias = listOf(
        Emergencia("RCP", "Reanimación cardiopulmonar", RcpActivity::class.java),
        Emergencia("Hemorragia", "Detener sangrado", HemorragiaActivity::class.java),
        Emergencia("Quemadura", "Atención urgente por quemaduras", QuemaduraActivity::class.java),
        Emergencia("Atragantamiento", "Obstrucción de las vías aéreas", AtragantamientoActivity::class.java)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.busqueda)

        // Referencias a los campos de texto busqueda
        val searchInput = findViewById<EditText>(R.id.searchInput)
        val filterInput = findViewById<EditText>(R.id.searchFilter)
        val grid = findViewById<GridLayout>(R.id.gridLayoutCards)

        // Función para mostrar cards según filtro
        fun mostrarEmergencias(query: String) {
            grid.removeAllViews()
            val filtradas = listaEmergencias.filter {
                it.nombre.contains(query, ignoreCase = true) ||
                        it.descripcion.contains(query, ignoreCase = true)
            }
            if (filtradas.isEmpty()) {
                val texto = TextView(this)
                texto.text = "No se encontraron emergencias"
                texto.textSize = 16f
                texto.gravity = Gravity.CENTER
                texto.setPadding(20, 70, 20, 70)
                grid.addView(texto)
            } else {
                for (item in filtradas) {
                    val card = LayoutInflater.from(this)
                        .inflate(R.layout.item_emergencia_card, grid, false)
                    card.findViewById<TextView>(R.id.txtNombreEmergencia).text = item.nombre
                    card.findViewById<TextView>(R.id.txtDescripcionEmergencia).text = item.descripcion
                    card.setOnClickListener {
                        startActivity(Intent(this, item.destino))
                    }
                    grid.addView(card)
                }
            }
        }

        findViewById<LinearLayout>(R.id.homeButtonLayout).setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finish() // Opcional, para cerrar la pantalla actual
        }

        findViewById<LinearLayout>(R.id.cardSearch).setOnClickListener {
            // Ya en la actividad de búsqueda, puedes dejarlo o mantenerlo para recargar
            startActivity(Intent(this, BusquedaActivity::class.java))
        }



        findViewById<LinearLayout>(R.id.profileButtonLayout).setOnClickListener {
            // Ajusta a tu actividad de perfil
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.MapaButtonLayout).setOnClickListener {
            // Aquí puedes iniciar la actividad deseada, por ejemplo:
            startActivity(Intent(this, MapaActivity::class.java))
        }



        // Mostrar todas las emergencias al inicio
        mostrarEmergencias("")

        // Escuchar cambios en las barras de búsqueda para actualizar las cards
        val watcher = object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = (searchInput.text.toString() + " " + filterInput.text.toString()).trim()
                mostrarEmergencias(query)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        }

        searchInput.addTextChangedListener(watcher)
        filterInput.addTextChangedListener(watcher)
    }
}
