package com.screentimex.nouzze.Activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.screentimex.nouzze.R
import com.screentimex.nouzze.databinding.ActivityAddAddressBinding
import com.screentimex.nouzze.models.Address
import com.screentimex.nouzze.models.ProfileDetails

class AddAddress : AppCompatActivity() {

    lateinit var binding : ActivityAddAddressBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableSpinner(false)
        binding.AddAddressButton.setOnClickListener {
            hideKeyboard()
            enableSpinner(true)
            addAddressToDatabase()
        }
    }

    private fun addAddressToDatabase(){
        val okCredentials = validateForm()
        if ( okCredentials.isBlank() ){
            storeAddress()
        }else{
            enableSpinner(false)
            Toast.makeText(this,"Error ${okCredentials}",Toast.LENGTH_SHORT).show()
//            error("Ritik")
        }
    }

    private fun storeAddress() {
        enableSpinner(false)
        val intent = Intent(this,com.screentimex.nouzze.Activities.Address::class.java)
        startActivity(intent)
//        val name = ""
//        val mobile = ""
//        val flat = ""
//        val area = ""
//        val lanmark = ""
//        val pincoe = ""
//        val city = ""
//        val state = ""
//        val userInfo = Address()
//        FirebaseFirestore.getInstance().collection("Address Information")
//            .document(getCurrentUUID())
//            .set(userInfo, SetOptions.merge()).addOnSuccessListener {
//                Log.e("MyTag", "Register")
//            }.addOnFailureListener {
//                    _ ->
//                Log.e("FireStoreClassSignUp","Error")
//            }
    }

    private fun validateForm(): String{
        var lis : String = ""
        if(binding.BuyerName.text.toString().isEmpty())  lis = "Name"
        else if(binding.MobileNumber.text.toString().isEmpty())    lis = "Mobile Number "
        else if(binding.FlatNumber.text.toString().isEmpty())     lis = "Flat Number"
        else if(binding.Area.text.toString().isEmpty())      lis = "Area"
        else if(binding.Landmark.text.toString().isEmpty() ) lis = "Landmark"
        else if (binding.Pincode.text.toString().isEmpty()) lis = "Pincode"
        else if(binding.City.text.toString().isEmpty()) lis = "City"
        else if(binding.State.text.toString().isEmpty()) lis = "State"
        return lis
    }
    fun showError(message: String){
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(
            ContextCompat.getColor(
                this@AddAddress,
                R.color.snackbarcolor
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
        binding.AddAddressButton.isEnabled = !enable
    }
    fun getCurrentUUID(): String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if(currentUser != null) {
            currentUserId = currentUser.uid
        }
        return currentUserId
    }
    fun hideKeyboard(){
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if ( inputManager.isAcceptingText ){
            inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken,0)
        }
    }
}