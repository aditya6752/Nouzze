package com.example.test.Services

import com.screentimex.nouzze.models.AddressDetails
import com.screentimex.nouzze.models.Category
import com.screentimex.nouzze.models.ProductDetails

object DataService {

    val categories = listOf(
        Category("SHIRTS" ,"shirts"),
        Category("HOODIES" , "hoodie"),
        Category("HATS", "hats"),
        Category("BOTTLES", "bottle")
    )

    val shirts = arrayListOf(
        ProductDetails("White Shirt" , "30",  "shirt1" ),
        ProductDetails("SLopes Light Grey" , "20","shirt2"),
        ProductDetails("SLopes Red" , "32", "shirt3"),
        ProductDetails("SLopes Hustle","29","shirt4")
    )

    val hoodies = arrayListOf (
        ProductDetails("Black Hoodie" , "28",  "hoodie1" ),
        ProductDetails("SLopes Red" , "20","hoodie2"),
        ProductDetails("SLopes Gray" , "22", "hoodie3"),
        ProductDetails("SLopes Black","34","hoodie4")
    )

    val hats = arrayListOf(
        ProductDetails("Classic Hat" , "18",  "hat1" ),
        ProductDetails("Black Slopes" , "20","hat2"),
        ProductDetails("White Slopes" , "22", "hat3"),
        ProductDetails("SLopes Snapback","24","hat4")
    )

    val bottles = arrayListOf(
        ProductDetails("Green Bottle" , "18",  "bottle" ),
        ProductDetails("Black Slopes" , "20","bottle1"),
        ProductDetails("White Slopes" , "22", "bottle2"),
        ProductDetails("SLopes Snapback","24","bottle3")
    )

    fun getProducts(category : String) : ArrayList<ProductDetails> {
        return when (category){ // we can also write return before each output ex : -> return shirts
            "SHIRTS" -> shirts
            "HATS" -> hats
            "BOTTLES" -> bottles
            else -> hoodies
        }
    }

}