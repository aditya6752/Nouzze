package com.screentimex.nouzze.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.Services.DataService
import com.screentimex.nouzze.Adapters.CategoryAdapter
import com.screentimex.nouzze.R
import com.screentimex.nouzze.databinding.ActivityStoreActivityBinding
import com.screentimex.nouzze.models.Category
import com.screentimex.nouzze.models.Constants

class StoreActivity : AppCompatActivity() {

    lateinit var categoryRecyclerView: RecyclerView
    lateinit var  categoriesList :ArrayList<Category>
    lateinit var CategoryName : String
    lateinit var  categoryAdapter : CategoryAdapter

    lateinit var binding : ActivityStoreActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpActionBar()
        categoryRecyclerView = findViewById(R.id.RecylerViewCategories)
        categoryRecyclerView.layoutManager = LinearLayoutManager(this)

        categoryAdapter = CategoryAdapter(DataService.categories,this)
        categoryRecyclerView.adapter = categoryAdapter

        categoryAdapter.setItemClickListener( object : CategoryAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(this@StoreActivity,Products::class.java)
                val productName = DataService.categories[position].categoryName
                intent.putExtra(Constants.PRODUCT_NAME, productName)
                startActivity(intent)
            }
        })
    }

    private fun setUpActionBar() {
        setSupportActionBar(binding.customToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Market Place"
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)
        binding.customToolBar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}
