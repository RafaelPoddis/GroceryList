package com.example.grocerylist

data class Lista(
    val id: Int,
    var name: String,
    var items: MutableList<Item> = mutableListOf()
)
