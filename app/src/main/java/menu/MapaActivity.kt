package com.aristidevs.finalmente.menu

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.aristidevs.finalmente.R
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.annotation.annotations
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

data class Hospital(val name: String, val lon: Double, val lat: Double, val address: String)

class MapaActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var pointAnnotationManager: PointAnnotationManager
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private val GEOAPIFY_API_KEY = "7c44b36712584840b23e218b0da22081"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)

        mapView = findViewById(R.id.mapView)

        // Inicializar el plugin de anotaciones correctamente
        pointAnnotationManager = mapView.annotations.createPointAnnotationManager()

        // Cargar el estilo y centrar mapa en Bogotá
        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS) {
            val bogota = Point.fromLngLat(-74.072092, 4.710989)
            mapView.getMapboxMap().setCamera(
                CameraOptions.Builder()
                    .center(bogota)
                    .zoom(13.0)
                    .build()
            )
        }

        // Botón para buscar hospitales
        findViewById<Button>(R.id.btnHospitals).setOnClickListener {
            Toast.makeText(this, "Buscando hospitales cercanos...", Toast.LENGTH_SHORT).show()
            buscarHospitales(-74.072092, 4.710989, 2000)
        }

        // Otros botones
        findViewById<Button>(R.id.btnPharmacies).setOnClickListener {
            Toast.makeText(this, "En desarrollo...", Toast.LENGTH_SHORT).show()
        }
        findViewById<Button>(R.id.btnPolice).setOnClickListener {
            Toast.makeText(this, "En desarrollo...", Toast.LENGTH_SHORT).show()
        }

        pedirPermisoUbicacion()
    }

    private fun pedirPermisoUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            habilitarUbicacion()
        }
    }

    private fun habilitarUbicacion() {
        // Aquí puedes mostrar la ubicación del usuario si quieres
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                habilitarUbicacion()
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun buscarHospitales(long: Double, lat: Double, radio: Int) {
        val url =
            "https://api.geoapify.com/v2/places?categories=healthcare.hospital&filter=circle:$long,$lat,$radio&apiKey=$GEOAPIFY_API_KEY"
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@MapaActivity, "Error al buscar hospitales", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { jsonStr ->
                    android.util.Log.d("MapaActivity", "Respuesta API Geoapify: $jsonStr")

                    try {
                        val json = JSONObject(jsonStr)
                        val features = json.getJSONArray("features")

                        val hospitals = mutableListOf<Hospital>()
                        for (i in 0 until features.length()) {
                            val prop = features.getJSONObject(i).getJSONObject("properties")
                            val geom = features.getJSONObject(i).getJSONObject("geometry")
                            val coords = geom.getJSONArray("coordinates")
                            hospitals.add(
                                Hospital(
                                    prop.optString("name", "Sin nombre"),
                                    coords.getDouble(0),
                                    coords.getDouble(1),
                                    prop.optString("address_line2", "")
                                )
                            )
                        }

                        runOnUiThread {
                            mostrarMarcadores(hospitals)
                        }

                    } catch (e: JSONException) {
                        android.util.Log.e("MapaActivity", "No se encontró 'features' en JSON: ", e)
                        runOnUiThread {
                            Toast.makeText(this@MapaActivity, "Error al obtener datos de hospitales", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        })
    }

    private fun mostrarMarcadores(hospitals: List<Hospital>) {
        pointAnnotationManager.deleteAll() // Limpiar marcadores previos

        hospitals.forEach { hospital ->
            val point = Point.fromLngLat(hospital.lon, hospital.lat)

            val pointAnnotationOptions = PointAnnotationOptions()
                .withPoint(point)
                .withTextField(hospital.name)

            pointAnnotationManager.create(pointAnnotationOptions)
        }

        Toast.makeText(this, "${hospitals.size} hospitales mostrados", Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }
}
