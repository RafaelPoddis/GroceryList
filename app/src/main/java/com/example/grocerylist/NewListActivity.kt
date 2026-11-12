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

    private lateinit var crud: Crud

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newlist)

        crud = Crud(this)
        val backBtn = findViewById<ImageButton>(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }
        val id = intent.getStringExtra("id") ?: ""

        if (!id.isEmpty()){

        }

        val saveBtn = findViewById<ImageButton>(R.id.saveBtn)
        val campoNome = findViewById<EditText>(R.id.nameField)

        saveBtn.setOnClickListener {
            if (id.isEmpty()){
                val nome = campoNome.text.toString().trim()
                if (nome.isEmpty()){
                    campoNome.error = "Coloque um nome na lista!"
                } else {
                    crud.create(Lista(nome))
                }
            }
        }

    }
}