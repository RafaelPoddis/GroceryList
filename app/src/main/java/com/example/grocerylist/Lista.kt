package com.example.grocerylist

data class Lista(
    var name: String = "",
    var items: List<Item> = emptyList()
)
