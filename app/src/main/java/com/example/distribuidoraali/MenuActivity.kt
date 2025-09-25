package com.example.distribuidoraali

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.distribuidoraali.databinding.ActivityMenuBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.io.IOException
import java.util.*
import kotlin.math.*

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var geocoder: Geocoder // Añadido: Instancia de Geocoder

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val DISTRIBUIDORA_LATITUDE = -33.0487
        private const val DISTRIBUIDORA_LONGITUDE = -71.5513
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        geocoder = Geocoder(this, Locale.getDefault()) // Añadido: Inicializa el Geocoder

        checkLocationPermission()

        binding.calculateButton.setOnClickListener {
            calcularDespacho()
        }
    }

    private fun calcularDespacho() {
        val totalCompraText = binding.totalCompraEditText.text.toString()
        Log.d("MenuActivity", "Calculando despacho...")

        if (totalCompraText.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa el monto de la compra.", Toast.LENGTH_SHORT).show()
            return
        }

        val totalCompra = totalCompraText.toDouble()

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permiso de ubicación no concedido. Por favor, actívalo en la configuración de la aplicación.", Toast.LENGTH_SHORT).show()
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                Log.d("MenuActivity", "Ubicación obtenida: ${location.latitude}, ${location.longitude}")
                val distanciaKm = calcularDistanciaKm(
                    DISTRIBUIDORA_LATITUDE,
                    DISTRIBUIDORA_LONGITUDE,
                    location.latitude,
                    location.longitude
                )
                val costoDespacho = calcularCostoDespacho(totalCompra, distanciaKm)

                val distanciaRedondeada = String.format("%.2f", distanciaKm).toDouble()
                val costoRedondeado = String.format("%.0f", costoDespacho).toDouble()

                binding.distanceTextView.text = "Distancia: $distanciaRedondeada km"
                binding.resultTextView.text = "Costo del Despacho: $$costoRedondeado CLP"

                // la logic para obtener la direccion del user en palabras sin lat. y long. d egps
                try {
                    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    if (!addresses.isNullOrEmpty()) {
                        val address = addresses[0]
                        val street = address.thoroughfare ?: "Calle desconocida"
                        val number = address.subThoroughfare ?: "S/N"
                        val city = address.locality ?: "Ciudad desconocida"
                        binding.locationTextView.text = "Ubicación: $street $number, $city"
                        Log.d("MenuActivity", "Dirección obtenida: $street $number, $city")
                    } else {
                        binding.locationTextView.text = "Ubicación: No se pudo obtener la dirección."
                        Log.d("MenuActivity", "No se encontró una dirección para las coordenadas.")
                    }
                } catch (e: IOException) {
                    binding.locationTextView.text = "Ubicación: Error al obtener la dirección."
                    Log.e("MenuActivity", "Error de geocodificación.", e)
                }

                guardarDatosDespachoEnFirebase(totalCompra, distanciaKm, costoDespacho)

            } else {
                Log.d("MenuActivity", "Ubicación nula. Usando valores de prueba.")
                Toast.makeText(this, "No se pudo obtener la ubicación. Usando 15 km de prueba.", Toast.LENGTH_SHORT).show()
                val distanciaKm = 15.0
                val costoDespacho = calcularCostoDespacho(totalCompra, distanciaKm)

                binding.distanceTextView.text = "Distancia: %.2f km".format(distanciaKm)
                binding.resultTextView.text = "Costo del Despacho: $$costoDespacho CLP"
                binding.locationTextView.text = "Ubicación: (no disponible, usando valores de prueba)"

                guardarDatosDespachoEnFirebase(totalCompra, distanciaKm, costoDespacho)
            }
        }
    }

    private fun guardarDatosDespachoEnFirebase(totalCompra: Double, distanciaKm: Double, costoDespacho: Double) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val despachoRef = database.getReference("despachos").child(userId).push()
            val despachoData = mapOf(
                "totalCompra" to totalCompra,
                "distanciaKm" to distanciaKm,
                "costoDespacho" to costoDespacho,
                "timestamp" to System.currentTimeMillis()
            )
            despachoRef.setValue(despachoData)
                .addOnSuccessListener {
                    Log.d("FirebaseDB", "Datos de despacho guardados con éxito.")
                }
                .addOnFailureListener { e ->
                    Log.e("FirebaseDB", "Error al guardar datos de despacho.", e)
                }
        }
    }

    fun calcularCostoDespacho(totalCompra: Double, distanciaKm: Double): Double {
        return when {
            totalCompra > 50000 -> 0.0
            totalCompra in 25000.0..49999.0 -> 150.0 * distanciaKm
            else -> 300.0 * distanciaKm
        }
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso de ubicación concedido. Ahora puedes calcular el despacho.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado. Usando ubicación de prueba.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun calcularDistanciaKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val radioTierraKm = 6371.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2).pow(2) + cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return radioTierraKm * c
    }
}