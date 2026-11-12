package com.example.grocerylist

//import android.hardware.Sensor
//import android.hardware.SensorEvent
//import android.hardware.SensorEventListener
//import android.hardware.SensorManager
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import org.w3c.dom.Text
import java.io.File

class MainActivity : AppCompatActivity(), LocationListener {

    private lateinit var locationManager : LocationManager
    private lateinit var crud: Crud
    private lateinit var listsView: LinearLayout
    private lateinit var marketsView: LinearLayout
    private lateinit var listsDisplay: ScrollView
    private lateinit var marketsDisplay: ScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        val items = mutableListOf(
            Item(id = 1, name = "Arroz", amount = 2),
            Item(id = 2, name = "Feijão", amount = 3)
        )

        crud = Crud(this)

        val botaoAddList = findViewById<Button>(R.id.addListBtn)

        botaoAddList.setOnClickListener {
            startActivity(Intent(this, NewListActivity::class.java))
        }

        val listaTeste = Lista("Teste 3", items)
//        crud.create(listaTeste)
        listsView = findViewById(R.id.listsView)
        crud.carregarListas(listsView)

        // ===== A partir daqui, é mercado handler =====
        marketsView = findViewById(R.id.marketsView)
        val mercados = listOf(
            Mercado("Carrefour", -23.197053, -45.903551),
            Mercado("Walmart", -23.219779, -45.888373),
            Mercado("Sam's Club", -23.204864, -45.911887)
        )

        for (mercado in mercados) {
            val mercadoView = layoutInflater.inflate(
                R.layout.activity_mercadosview,
                marketsView,
                false
            )

            val nomeText = mercadoView.findViewById<TextView>(R.id.mercadoNome)
            nomeText.text = mercado.nome

            marketsView.addView(mercadoView)
        }

        val homeBtn: Button = findViewById(R.id.homeBtn)
        val marketsBtn: Button = findViewById(R.id.marketsBtn)
        listsDisplay = findViewById(R.id.displayLists)
        marketsDisplay = findViewById(R.id.displayMarkets)

        homeBtn.setOnClickListener {
            showListsScreen()
        }

        marketsBtn.setOnClickListener {
            showMarketsScreen()
        }
    }

    private fun showListsScreen() {
        listsDisplay.visibility = View.VISIBLE
        marketsDisplay.visibility = View.GONE
    }

    private fun showMarketsScreen() {
        listsDisplay.visibility = View.GONE
        marketsDisplay.visibility = View.VISIBLE
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
