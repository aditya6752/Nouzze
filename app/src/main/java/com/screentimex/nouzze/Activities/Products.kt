package com.screentimex.nouzze.Activities

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.Services.DataService
import com.screentimex.nouzze.Adapters.ProductAdapter
import com.screentimex.nouzze.R
import com.screentimex.nouzze.Services.ProductDetailSharedPref
import com.screentimex.nouzze.databinding.ActivityProductsBinding
import com.screentimex.nouzze.models.Constants
import com.screentimex.nouzze.models.ProductDetails

class Products : AppCompatActivity() {

    lateinit var myRecyclerView: RecyclerView
    lateinit var productDetailsList : ArrayList<ProductDetails>
    lateinit var productDetailSharedPrefProducts : ProductDetailSharedPref

    private lateinit var binding: ActivityProductsBinding
    private lateinit var productName: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // calling intent here because product name needed for setting up action bar
        productName = intent.getStringExtra(Constants.PRODUCT_NAME).toString()
        setUpActionBar()

        productDetailSharedPrefProducts = ProductDetailSharedPref(this)

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
        productDetailsList = DataService.getProducts(productName)
        myRecyclerView.adapter = ProductAdapter(productDetailsList,this@Products)

    }

    private fun setUpActionBar() {
        setSupportActionBar(binding.customToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val title = productName.substring(0, 1).toUpperCase() + productName.substring(1).toLowerCase()
        supportActionBar?.title = title
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)
        binding.customToolBar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == 101) {
            productDetailSharedPrefProducts.clearPreference()
        }
    }
}