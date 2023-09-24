package com.screentimex.nouzze.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.Services.DataService
import com.screentimex.nouzze.Adapters.AddressAdapter
import com.screentimex.nouzze.R
import com.screentimex.nouzze.databinding.ActivityAddressBinding

class Address : AppCompatActivity() {

    lateinit var binding : ActivityAddressBinding
    lateinit var AddressRecyclerView : RecyclerView
    lateinit var AddressList : ArrayList<Address>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addAddressButton.setOnClickListener {
            var intent = Intent(this,AddAddress::class.java)
            startActivity(intent)
        }

        AddressRecyclerView = findViewById(R.id.Address_RecyclerView)

        AddressRecyclerView.layoutManager = LinearLayoutManager(this)

        var AddressAdapter = AddressAdapter(DataService.Addressess,this)
        AddressRecyclerView.adapter = AddressAdapter

    }
}