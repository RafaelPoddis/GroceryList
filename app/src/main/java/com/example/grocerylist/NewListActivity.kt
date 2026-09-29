package com.example.grocerylist

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class NewListActivity: AppCompatActivity() {

    private lateinit var crud: ViewLists

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_newlist)

        val backBtn = findViewById<ImageButton>(R.id.backBtn)
        crud = ViewLists(this)
        val listaId = intent.getIntExtra("listaId", -1)

        backBtn.setOnClickListener {
            val listaNome = findViewById<EditText>(R.id.nameField).text.toString()

            if(listaId == -1){
                crud.update(listaId, listaNome)
            } else {
                crud.create(listaNome)
            }
            finish()
        }
    }
}