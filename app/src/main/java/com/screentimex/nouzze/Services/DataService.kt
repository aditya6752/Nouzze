package com.example.test.Services

import com.screentimex.nouzze.models.Address
import com.screentimex.nouzze.models.Category
import com.screentimex.nouzze.models.Product

object DataService {

    val categories = listOf(
        Category("SHIRTS" ,"shirts"),
        Category("HOODIES" , "hoodie"),
        Category("HATS", "hats"),
        Category("BOTTLES", "bottle")
    )

    val Addressess = arrayListOf(
        Address("Ritik Rawat","9999999999","183-B","Mayur Vihar Ph-3","Ghazipur Famous Hills"
        ,"110096","East Delhi","Delhi")
    )

    val shirts = arrayListOf(
        Product("White Shirt" , "$30",  "shirts" ),
        Product("SLopes Light Grey" , "$20","shirt2"),
        Product("SLopes Red" , "$32", "shirt3"),
        Product("SLopes Hustle","$29","shirt4")
    )

    val hoodies = arrayListOf (
        Product("Black Hoodie" , "$28",  "hoodie" ),
        Product("SLopes Red" , "$20","hoodie2"),
        Product("SLopes Gray" , "$22", "hoodie3"),
        Product("SLopes Black","$34","hoodie4")
    )

    val hats = arrayListOf(
        Product("Classic Hat" , "$18",  "hats" ),
        Product("Black Slopes" , "$20","hat2"),
        Product("White Slopes" , "$22", "hat3"),
        Product("SLopes Snapback","$24","hat4")
    )

    val bottles = arrayListOf(
        Product("Green Bottle" , "$18",  "bottle" ),
        Product("Black Slopes" , "$20","hat2"),
        Product("White Slopes" , "$22", "hat3"),
        Product("SLopes Snapback","$24","hat4")
    )

    fun getProducts(category : String) : ArrayList<Product> {
        return when (category ){ // we can also write return before each output ex : -> return shirts
            "SHIRTS" -> shirts
            "HATS" -> hats
            "BOTTLES" -> bottles
            else -> hoodies
        }
    }

}