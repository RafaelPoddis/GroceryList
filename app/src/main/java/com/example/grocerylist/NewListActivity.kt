package com.example.grocerylist

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NewListActivity: AppCompatActivity() {

    private val dbRef: DatabaseReference = FirebaseDatabase
        .getInstance()
        .getReference("listas")
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
        val saveBtn = findViewById<ImageButton>(R.id.saveBtn)
        val campoNome = findViewById<EditText>(R.id.nameField)

        if (!id.isEmpty()){
            dbRef.addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val listas = mutableListOf<ListaComID>()
                    for (item in snapshot.children) {
                        val chave = item.key
                        val lista = item.getValue(Lista::class.java)
                        if (id == item.key) {
                            campoNome.setText(item.getValue(Lista::class.java)?.name)
                        }
                        if(chave != null && lista != null){
                            listas.add(
                                ListaComID(chave, lista)
                            )
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Abrir lista especifica", "Nao deu pra abrir essa lista")
                    Toast.makeText(this@NewListActivity, "Erro ao carregar a lista!", Toast.LENGTH_SHORT).show()
                }

            })
        }


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