package com.screentimex.nouzze.Activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.screentimex.nouzze.Adapters.AddressAdapter
import com.screentimex.nouzze.Firebase.FireStoreClass
import com.screentimex.nouzze.R
import com.screentimex.nouzze.databinding.ActivityAddressBinding
import com.screentimex.nouzze.models.AddressDetails

class Address : AppCompatActivity() {

    lateinit var binding : ActivityAddressBinding
    lateinit var AddressRecyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpActionBar()
        if ( !isInternetConnected(this) ){
            showSnackBar("No Internet !!")
        }
        binding.addOrUpdateAddressButton.setOnClickListener {
            startActivity(Intent(this@Address, AddAddress::class.java))
        }
        binding.proceedToBuy.setOnClickListener {
            val intent = Intent(this,CheckOut::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        FireStoreClass().getAddress(this@Address)
    }
    // default address present in firebase -> update recycler view and Billing Button - Update Address
    fun getAddressFromDatabase(addressDetails: AddressDetails) {
        binding.apply {
            AddressRecyclerView.visibility = View.VISIBLE
            noAddressTestView.visibility = View.GONE
            binding.proceedToBuy.isEnabled = true
        }
        binding.addOrUpdateAddressButton.text = "Update Address"
        setUpRecyclerView(addressDetails)
    }

    // No address saved -> Billing button unClickable - Add Address
    fun noAddressSaved() {
        showSnackBar("No Address Saved")
        binding.apply {
            AddressRecyclerView.visibility = View.GONE
            noAddressTestView.visibility = View.VISIBLE
            binding.proceedToBuy.isEnabled = false
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
            setResult(Activity.RESULT_OK)
            onBackPressed()
        }
    }

    fun showSnackBar(message: String){
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(
            ContextCompat.getColor(
                this@Address,
                R.color.snackBarColor
            )
        )
        snackBar.show()
    }
    private fun isInternetConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}