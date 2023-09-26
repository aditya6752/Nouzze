package com.screentimex.nouzze.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.Services.DataService
import com.screentimex.nouzze.Adapters.AddressAdapter
import com.screentimex.nouzze.Firebase.FireStoreClass
import com.screentimex.nouzze.R
import com.screentimex.nouzze.databinding.ActivityAddressBinding
import com.screentimex.nouzze.models.AddressDetails
import com.screentimex.nouzze.models.Constants

class Address : AppCompatActivity() {

    lateinit var binding : ActivityAddressBinding
    lateinit var AddressRecyclerView : RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpActionBar()

        FireStoreClass().getAddress(this@Address)

        binding.addOrUpdateAddressButton.setOnClickListener {
            startActivity(Intent(this@Address, AddAddress::class.java))
        }
    }

    // default address present in firebase -> update recycler view and Billing Button - Update Address
    fun getAddressFromDatabase(addressDetails: AddressDetails) {
        binding.apply {
            AddressRecyclerView.visibility = View.VISIBLE
            noAddressTestView.visibility = View.GONE
            // todo billing address button functionality change
        }
        binding.addOrUpdateAddressButton.text = "Update Address"
        setUpRecyclerView(addressDetails)
    }

    // No address saved -> Billing button unClickable - Add Address
    fun noAddressSaved() {
        binding.apply {
            AddressRecyclerView.visibility = View.GONE
            noAddressTestView.visibility = View.VISIBLE
            // todo billing address button functionality change
        }
    }
    private fun setUpRecyclerView(addressDetails: AddressDetails) {
        AddressRecyclerView = findViewById(R.id.Address_RecyclerView)
        AddressRecyclerView.layoutManager = LinearLayoutManager(this)
        val AddressAdapter = AddressAdapter(arrayListOf(addressDetails),this)
        AddressRecyclerView.adapter = AddressAdapter
    }
    private fun setUpActionBar() {
        setSupportActionBar(binding.customToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Saved Address"
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)
        binding.customToolBar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}