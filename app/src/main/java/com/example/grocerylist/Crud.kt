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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import androidx.appcompat.app.AlertDialog

class Crud(private val context: Context) {

    private val dbRef: DatabaseReference = FirebaseDatabase
        .getInstance()
        .getReference("listas")

    private lateinit var itemView: View
    private var listasSalvas = emptyList<ListaComID>()
    private var itensSalvos = emptyList< ItensComId>()

    // Cria a lista no firebase
    fun createLista(lista: Lista): String {
        val key = dbRef.push()

        key.setValue(lista)
            .addOnSuccessListener {
                Toast.makeText(context, "Lista criada com sucesso!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Log.e("Firebase", "Deu um erro aí!")
            }

        return key.key!!
    }

    fun createItem(item: Item, listaKey: String) {
        dbRef.child("${listaKey}/items")
            .push()
            .setValue(item)
            .addOnSuccessListener {
                Toast.makeText(context, "Item criado!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Log.e("Firebase", "Deu ruim no item")
            }
    }
    fun deleteItem(listaId: String, itemId: String) {
        val itemsRef = dbRef.child("${listaId}/items/${itemId}")
        itemsRef.removeValue()
            .addOnSuccessListener {
                Toast.makeText(context, "Item excluido com sucesso!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Log.e("Firebase", "Excluir item firebase")
                Toast.makeText(context, "Erro ao excluir item!", Toast.LENGTH_SHORT).show()
            }
    }
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