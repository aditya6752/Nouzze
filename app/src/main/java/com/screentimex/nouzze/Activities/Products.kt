package com.screentimex.nouzze.Activities

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.Services.DataService
import com.screentimex.nouzze.Adapters.ProductAdapter
import com.screentimex.nouzze.R
import com.screentimex.nouzze.models.Product

class Products : AppCompatActivity() {

    lateinit var myRecyclerView: RecyclerView
    lateinit var productList : ArrayList<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        myRecyclerView = findViewById(R.id.RecylerViewProducts)


        var spanCount = 2
        val orientation = resources.configuration.orientation
        if ( orientation == Configuration.ORIENTATION_LANDSCAPE ){
            spanCount = 3
        }

        val screenSize = resources.configuration.screenWidthDp
        if ( screenSize > 720 ){
            spanCount = 3
        }



        val layoutManager = GridLayoutManager(this , spanCount)
        myRecyclerView.layoutManager = layoutManager


        val cat = intent.getStringExtra("cat").toString()

        productList = DataService.getProducts(cat)
        myRecyclerView.adapter = ProductAdapter(productList,this)

    }
}