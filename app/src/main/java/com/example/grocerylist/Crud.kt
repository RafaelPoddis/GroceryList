package com.example.grocerylist

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import androidx.appcompat.app.AlertDialog

class Crud(private val context: Context) {

    private val dbRef: DatabaseReference = FirebaseDatabase
        .getInstance()
        .getReference("listas")

    private lateinit var itemView: View
    private var listasSalvas = emptyList<ListaComID>()
    private var itensSalvos = emptyList< ItensComId>()

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
    fun deleteLista() {

    }

    fun deleteItem(listaId: String, itemId: String) {
        val itemsRef = dbRef.child("${listaId}/items")
        itemsRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (items in snapshot.children) {
                    if (items.key == itemId) {
                        itemsRef.child("itemId")
                            .removeValue()
                            .addOnSuccessListener {
                                Toast.makeText(context, "Item excluido com sucesso!", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                Log.e("Firebase", "Excluir item firebase")
                                Toast.makeText(context, "Erro ao excluir item!", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Log.e("Firebase", "Nao achou")
                        Toast.makeText(context, "Erro ao excluir item!", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Erro no banco de dados!", Toast.LENGTH_SHORT).show()
            }
        })
    }
//
//    fun addItem() {
//        TODO()
//    }
//
//    private fun saveToFile() {
//        TODO()
//    }

    // Leitura de todas as listas

    fun printListas(listsView: LinearLayout) {
        for (item in listasSalvas){
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

            val deletarListaBtn = itemView.findViewById<ImageButton>(R.id.deleteBtn)
            deletarListaBtn.setOnClickListener {
                AlertDialog.Builder(context)
                    .setTitle("Tem certeza que deseja excluir?")
                    .setMessage("${item.lista.name} - ${item.lista.items.size} itens")
                    .setPositiveButton("Sim") {_,_ ->
                        dbRef.child(item.id)
                            .removeValue()
                            .addOnSuccessListener {
                                Toast.makeText(context, "Lista excluída com sucesso", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Erro ao excluir lista", Toast.LENGTH_SHORT).show()
                            }
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            }
            listsView.addView(itemView)
        }
    }

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
                listasSalvas = listas

                listsView.removeAllViews()
                val texto = TextView(context)
                if (listas.isEmpty()){
                    texto.text = "Você não possui nenhuma lista!"
                    listsView.addView(texto)
                } else {
                    printListas(listsView)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Deu ruim")
                Toast.makeText(context, "Erro ao carregar listas!", Toast.LENGTH_SHORT).show()
            }

        })
    }
}