package com.example.grocerylist

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class ViewLists(private val context: Context) {

    private val listas = mutableListOf<Lista>()
    private var nextId = 1
    private val fileName = "listas.json"

    init {
        loadFromFile()
    }

    fun create(name: String, items: MutableList<Item> = mutableListOf()) {
        val lista = Lista(id = nextId, name = name, items = items)
        listas.add(lista)
        nextId++
        saveToFile()
    }

    fun read(): List<Lista> {
        return listas
    }

    fun update(id: Int, newName: String) {
        val lista = listas.find { it.id == id }
        if (lista != null) {
            lista.name = newName
            saveToFile()
        }
    }

    fun delete(id: Int) {
        listas.removeAll { it.id == id }
        saveToFile()
    }

    fun addItem(listaId: Int, item: Item) {
        val lista = listas.find { it.id == listaId }
        lista?.items?.add(item)
        saveToFile()
    }

    private fun saveToFile() {
        val jsonArray = JSONArray()

        for (lista in listas) {
            val jsonObject = JSONObject()
            jsonObject.put("id", lista.id)
            jsonObject.put("name", lista.name)

            val itemsArray = JSONArray()
            for (item in lista.items) {
                val itemObj = JSONObject()
                itemObj.put("id", item.id)
                itemObj.put("name", item.name)
                itemObj.put("amount", item.amount)
                itemsArray.put(itemObj)
            }
            jsonObject.put("items", itemsArray)

            jsonArray.put(jsonObject)
        }

        val file = File(context.filesDir, fileName)
        file.writeText(jsonArray.toString())
    }

    private fun loadFromFile() {
        val file = File(context.filesDir, fileName)
        if (!file.exists()) return

        val jsonString = file.readText()
        if (jsonString.isBlank()) return

        listas.clear()
        val jsonArray = JSONArray(jsonString)

        var maxId = 0
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val id = jsonObject.getInt("id")
            val name = jsonObject.getString("name")

            val items = mutableListOf<Item>()
            val itemsArray = jsonObject.optJSONArray("items")
            if (itemsArray != null) {
                for (j in 0 until itemsArray.length()) {
                    val itemObj = itemsArray.getJSONObject(j)
                    val itemId = itemObj.getInt("id")
                    val itemNome = itemObj.getString("nome")
                    val itemAmount = itemObj.getInt("amount")
                    items.add(Item(id = itemId, name = itemNome, amount = itemAmount))
                }
            }

            listas.add(Lista(id = id, name = name, items = items))
            if (id > maxId) maxId = id
        }

        nextId = maxId + 1
    }
}