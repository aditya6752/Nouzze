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
        ProductDetails("White Shirt" , "30000",  "shirt1" ),
        ProductDetails("SLopes Light Grey" , "20000","shirt2"),
        ProductDetails("SLopes Red" , "32000", "shirt3"),
        ProductDetails("SLopes Hustle","29000","shirt4")
    )

    val hoodies = arrayListOf (
        ProductDetails("Black Hoodie" , "28000",  "hoodie1" ),
        ProductDetails("SLopes Red" , "20000","hoodie2"),
        ProductDetails("SLopes Gray" , "22000", "hoodie3"),
        ProductDetails("SLopes Black","34000","hoodie4")
    )

    val hats = arrayListOf(
        ProductDetails("Classic Hat" , "18000",  "hat1" ),
        ProductDetails("Black Slopes" , "20000","hat2"),
        ProductDetails("White Slopes" , "22000", "hat3"),
        ProductDetails("SLopes Snapback","24000","hat4")
    )

    val bottles = arrayListOf(
        ProductDetails("Green Bottle" , "18000",  "bottle" ),
        ProductDetails("Black Slopes" , "20000","bottle1"),
        ProductDetails("White Slopes" , "22000", "bottle2"),
        ProductDetails("SLopes Snapback","24000","bottle3")
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