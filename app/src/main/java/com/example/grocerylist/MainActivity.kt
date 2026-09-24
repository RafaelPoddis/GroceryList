package com.example.grocerylist

//import android.hardware.Sensor
//import android.hardware.SensorEvent
//import android.hardware.SensorEventListener
//import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), LocationListener {

    private lateinit var locationManager : LocationManager

    data class Mercado(
        val nome: String,
        val latitude: Double,
        val longitude: Double
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        val mercados = listOf(
            Mercado("Carrefour", -23.197053, -45.903551),
            Mercado("Walmart", -23.219779, -45.888373),
            Mercado("Sam's Club", -23.204864, -45.911887)
        )

        val homeBtn: Button = findViewById<Button>(R.id.homeBtn)
        val marketsBtn: Button = findViewById<Button>(R.id.marketsBtn)

    }

    override fun onLocationChanged(location: Location) {
        val latitude = location.latitude
        val longitude = location.longitude
        // Update your UI or ViewModel here
    }

    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
    @Deprecated("Deprecated in Java")
    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
}
