package com.example.grocerylist

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Text
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue

class Crud(private val context: Context) {

    private val dbRef: DatabaseReference = FirebaseDatabase
        .getInstance()
        .getReference("listas")

    private lateinit var itemView: View
//    init {
//        carregarListas()
//    }

    // Cria a lista no firebase
    fun create(lista: Lista) {
        dbRef.push()
            .setValue(lista)
            .addOnSuccessListener {
                Toast.makeText(context, "Lista criada com sucesso!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Log.e("Firebase", "Deu um erro aí!")
            }
    }

    // Ler apenas uma lista
//    fun read() {
//        TODO()
//    }

//    fun update() {
//        TODO()
//    }
//
//    fun delete() {
//        TODO()
//    }
//
//    fun addItem() {
//        TODO()
//    }
//
//    private fun saveToFile() {
//        TODO()
//    }

    // Leitura de todas as listas
    fun carregarListas(listsView: LinearLayout) {
        dbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("Firebase UP", "Deu bom a principio")
                val listas = mutableListOf<ListaComID>()

                for (item in snapshot.children) {
                    val id = item.key
                    val lista = item.getValue(Lista::class.java)

                    if (id != null && lista != null) {
                        listas.add(
                            ListaComID(id, lista)
                        )
                    }
                }
                
                val linhas = listas.mapIndexed { index, item ->
                    "${index} - ${item.lista.name}"
                }

                listsView.removeAllViews()
                val texto = TextView(context)
                if (linhas.isEmpty()){
                    texto.text = "Você não possui nenhuma lista!"
                    listsView.addView(texto)
                } else {
                    for (item in listas){
                        itemView = LayoutInflater.from(context).inflate(
                            R.layout.activity_listasview,
                            listsView,
                            false
                        )

                        val nomeText = itemView.findViewById<TextView>(R.id.listaNome)
                        nomeText.text = item.lista.name

                        val amountText = itemView.findViewById<TextView>(R.id.qntdItem)
                        amountText.text = item.lista.items.size.toString()

                        val verLista = itemView.findViewById<LinearLayout>(R.id.listaClicavel)
                        verLista.setOnClickListener {
                            val intent = Intent(context, NewListActivity::class.java)
                            intent.putExtra("listaId", item.id)
                            context.startActivity(intent)
                        }
                        listsView.addView(itemView)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Deu ruim")
                Toast.makeText(context, "Erro ao carregar listas!", Toast.LENGTH_SHORT).show()
            }

        })
    }
}