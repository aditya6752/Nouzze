package com.screentimex.nouzze.Activities

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.screentimex.nouzze.Firebase.FireStoreClass
import com.screentimex.nouzze.R
import com.screentimex.nouzze.databinding.ActivityAddAddressBinding
import com.screentimex.nouzze.models.AddressDetails

class AddAddress : AppCompatActivity() {
    private lateinit var binding : ActivityAddAddressBinding
    private lateinit var currentSavedAddressDetails: AddressDetails
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpActionBar()
        enableSpinner(false)
        if ( !isInternetConnected(this) ) {
            showSnackBar("No Internet !!")
        }
        FireStoreClass().getAddress(this@AddAddress)

        binding.addOrUpdateAddressButton.setOnClickListener {
            hideKeyboard()
            if ( isInternetConnected(this) ) {
                enableSpinner(true)
                addOrUpdateAddressToDatabase()
            }else{
                showSnackBar("No Internet !!")

            }
        }
    }

    private fun addOrUpdateAddressToDatabase(){
        val okCredentials = validateForm()
        if ( okCredentials.isBlank() ){
            storeAddress()
        }else{
            enableSpinner(false)
            showSnackBar(okCredentials)
        }
    }

    private fun storeAddress() {
        enableSpinner(false)
        val addressDetails = AddressDetails(
            binding.name.text.toString(),
            binding.mobileNumber.text.toString(),
            binding.flatNumber.text.toString(),
            binding.area.text.toString(),

            binding.landmark.text.toString(),
            binding.pincode.text.toString(),
            binding.city.text.toString(),
            binding.state.text.toString()
        )
        if(binding.mobileNumber.text.toString().length != 10) {
            showSnackBar("Enter Valid Mobile Number")
            return
        }
        if(binding.pincode.text.toString().length != 6) {
            showSnackBar("Enter Valid Pin-Code")
            return
        }
        currentSavedAddressDetails = addressDetails
        FireStoreClass().addOrUpdateAddress(this@AddAddress, addressDetails)
    }

    // add or update successfully
    fun addressAddedUpdateSuccessfully() {
        binding.addOrUpdateAddressButton.text = "Update Address"
        finish()

    }

    // address already in firebase
    fun populateActivity(addressDetails: AddressDetails) {
        binding.name.setText(addressDetails.Name)
        binding.mobileNumber.setText(addressDetails.Mobile_Number)
        binding.flatNumber.setText(addressDetails.Flat_Number)
        binding.area.setText(addressDetails.Area)
        binding.landmark.setText(addressDetails.Landmark)
        binding.pincode.setText(addressDetails.Pincode)
        binding.city.setText(addressDetails.City)
        binding.state.setText(addressDetails.State)
        binding.addOrUpdateAddressButton.text = "Update Address"
    }
    // no default address added to firebase
    fun noAddressInDatabase() {
        binding.addOrUpdateAddressButton.text = "Add Address"
    }

    private fun validateForm(): String{
        var lis : String = ""
        if(binding.name.text.toString().isEmpty())  lis = "Name"
        else if(binding.mobileNumber.text.toString().isEmpty())    lis = "Mobile Number "
        else if(binding.flatNumber.text.toString().isEmpty())     lis = "Flat Number"
        else if(binding.area.text.toString().isEmpty())      lis = "Area"
        else if(binding.landmark.text.toString().isEmpty() ) lis = "Landmark"
        else if (binding.pincode.text.toString().isEmpty()) lis = "Pincode"
        else if(binding.city.text.toString().isEmpty()) lis = "City"
        else if(binding.state.text.toString().isEmpty()) lis = "State"
        return lis
    }
    fun showSnackBar(message: String){
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(
            ContextCompat.getColor(
                this@AddAddress,
                R.color.snackBarColor
            )
        )
        snackBar.show()
    }
    fun enableSpinner(enable: Boolean ){

        if ( enable ){
            binding.createSpinner.visibility = View.VISIBLE
        }else{
            binding.createSpinner.visibility = View.INVISIBLE
        }
        binding.addOrUpdateAddressButton.isEnabled = !enable
    }
    fun getCurrentUUID(): String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if(currentUser != null) {
            currentUserId = currentUser.uid
        }
        return currentUserId
    }
    private fun hideKeyboard(){
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if ( inputManager.isAcceptingText ){
            inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken,0)
        }
    }

    private fun setUpActionBar() {
        setSupportActionBar(binding.customToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Add Address"
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)
        binding.customToolBar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
    private fun isInternetConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}