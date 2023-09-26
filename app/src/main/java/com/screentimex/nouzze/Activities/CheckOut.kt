package com.screentimex.nouzze.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.auth.User
import com.screentimex.nouzze.Adapters.AddressAdapter
import com.screentimex.nouzze.Firebase.FireStoreClass
import com.screentimex.nouzze.R
import com.screentimex.nouzze.databinding.ActivityCheckOutBinding
import com.screentimex.nouzze.models.AddressDetails
import com.screentimex.nouzze.models.Constants
import com.screentimex.nouzze.models.ProductDetails
import com.screentimex.nouzze.models.ProfileDetails

class CheckOut : AppCompatActivity() {

    lateinit var productDetails : ProductDetails
    lateinit var binding : ActivityCheckOutBinding
    lateinit var mAddressDetails : AddressDetails
    lateinit var mUserDetails: ProfileDetails
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckOutBinding.inflate(layoutInflater)
        setContentView(binding.root)



        if ( intent.hasExtra(Constants.PRODUCTDETAILS)) {
            productDetails = intent.getParcelableExtra<ProductDetails>(Constants.PRODUCTDETAILS)!!
        }

        FireStoreClass().getAddress(this)
        FireStoreClass().loadUserData(this)

        binding.productName.text = productDetails.productName
        binding.productPrice.text = productDetails.productPrice
        val imgId = this.resources.getIdentifier(productDetails.productImg,"drawable",this.packageName)
        binding.productImg.setImageResource(imgId)

        binding.BuyNowButton.setOnClickListener {
            
        }

    }

    fun populateAdress(addressDetails : AddressDetails ){
        mAddressDetails = addressDetails
        binding.UserName.setText(addressDetails.Name)
        binding.UserMobileNumber.setText(addressDetails.Mobile_Number)
        binding.UserAreaFlatNumber.setText("${addressDetails.Flat_Number} / ${addressDetails.Area} / ${addressDetails.Landmark} ")
        binding.UserCityStatePin.setText("${addressDetails.City} / ${addressDetails.State} / ${addressDetails.Pincode}")
    }
    private fun setUpActionBar() {
        setSupportActionBar(binding.customToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Checkout"
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)
        binding.customToolBar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
    fun getUserData(UserData : ProfileDetails ){
        mUserDetails = UserData
        binding.userNameWelcome.text = mUserDetails.name
    }
}