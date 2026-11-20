package com.example.grocerylist

import android.os.Bundle
import android.os.Environment
import android.os.PersistableBundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import kotlin.time.Duration.Companion.milliseconds

class NewListActivity: AppCompatActivity() {

    private val dbRef: DatabaseReference = FirebaseDatabase
        .getInstance()
        .getReference("listas")
    private lateinit var crud: Crud
    private lateinit var listaKey: String
    private lateinit var itemInflate: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newlist)

        crud = Crud(this)

        val backBtn = findViewById<ImageButton>(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }

        listaKey = intent.getStringExtra("listaId") ?: ""
        val criarListaBtn = findViewById<Button>(R.id.createList)
        val campoNome = findViewById<EditText>(R.id.nameField)
        val addItemBtn = findViewById<Button>(R.id.addItemBtn)
        val linearContainer = findViewById<LinearLayout>(R.id.newItems)

        if (!listaKey.isEmpty()){
            criarListaBtn.visibility = View.GONE
            addItemBtn.visibility = View.VISIBLE
        }
        criarListaBtn.setOnClickListener {
            val nome = campoNome.text.toString().trim()
            if (nome.isEmpty()) {
                campoNome.error = "Coloque um nome na lista!"
            } else {
                listaKey = crud.create(Lista(nome))

                criarListaBtn.visibility = View.GONE
                addItemBtn.visibility = View.VISIBLE
            }
        }


        addItemBtn.setOnClickListener {
            val nomeItem = EditText(this@NewListActivity).apply {
                hint = "Nome do item"
                inputType = android.text.InputType.TYPE_CLASS_TEXT
            }

            val amountItem = EditText(this@NewListActivity).apply {
                hint = "Quantidade"
                inputType = android.text.InputType.TYPE_CLASS_NUMBER
            }

            val linear = LinearLayout(this@NewListActivity).apply {
                orientation = LinearLayout.HORIZONTAL
                setVerticalGravity(Gravity.CENTER)
                setHorizontalGravity(Gravity.CENTER)
                addView(nomeItem)
                addView(amountItem)
            }

            AlertDialog.Builder(this@NewListActivity)
                .setTitle("Adicionar Item")
                .setMessage("Novo item:")
                .setView(linear)
                .setPositiveButton("Salvar") { _, _ ->
                    val novoItemNome = nomeItem.text.toString()
                    val novoAmountItem = amountItem.text.toString().toIntOrNull()

                    if (novoAmountItem == null || novoAmountItem < 0) {
                        amountItem.error = "Valor inválido!"
                        Toast.makeText(
                            this@NewListActivity,
                            "Valor inválido!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (novoItemNome.isEmpty()) {
                        nomeItem.error = "Valor inválido!"
                        Toast.makeText(
                            this@NewListActivity,
                            "Valor inválido!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        crud.createItem(Item(novoItemNome, novoAmountItem), listaKey)
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
        dbRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    linearContainer.removeAllViews()

                    for (lista in snapshot.children) {
                        val listaChave = lista.key
                        val listaInfo = lista.getValue(Lista::class.java)

                        if (listaChave != null && listaInfo != null) {
                            if(!listaKey.isEmpty() && listaKey == listaChave) {
                                campoNome.hint = listaInfo.name

                                for (item in listaInfo.items) {
                                    val itemKey = item.key
                                    val itemInfo = item.value

                                    itemInflate = layoutInflater.inflate(
                                        R.layout.activity_listasview,
                                        linearContainer,
                                        false
                                    )
                                    val productName =
                                        itemInflate.findViewById<TextView>(R.id.listaNome)
                                    productName.text = itemInfo.name

                                    val productAmount =
                                        itemInflate.findViewById<TextView>(R.id.qntdItem)
                                    productAmount.text = itemInfo.amount.toString()
                                    linearContainer.addView(itemInflate)
                                }
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@NewListActivity,
                        "Nao deu pra mostrar os itens!",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })
    }
}