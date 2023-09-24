package com.screentimex.nouzze.Authentication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.screentimex.nouzze.R
import com.screentimex.nouzze.databinding.ActivityCreateUserBinding
import com.screentimex.nouzze.models.Constants
import com.screentimex.nouzze.models.ProfileDetails

class CreateUserActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCreateUserBinding
    private lateinit var mFireStore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpActionBar()
        mFireStore = FirebaseFirestore.getInstance()
        binding.signUpButton.setOnClickListener {
            hideKeyboard()
            binding.progressBarButton.visibility = View.VISIBLE
            registerUser()
        }
    }

    private fun registerUser(){
        val okCredentials = validateForm()
        if(okCredentials.isBlank()){
            val name = binding.userNameTextView.text.toString().trim{ it<=' '}
            val email = binding.emailTextView.text.toString().trim{ it<=' '}.toLowerCase()
            val password = binding.passwordTextView.text.toString().trim{ it<=' '}
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        val user = ProfileDetails(firebaseUser.uid, email, name)
                        FireStoreClass().registerUser(this, user)
                        Toast.makeText(this,"User Registered!!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, Drawer::class.java))
                    }
                    else{
                        //hideProgressDialog()
                        binding.progressBarButton.visibility = View.GONE
                        showError(task.exception!!.message!!)
                    }
                }
        }
        else{
            binding.progressBarButton.visibility = View.GONE
            showError(okCredentials)
        }
    }

    private fun validateForm(): String{
        var lis: String = ""
        if(binding.userNameTextView.text.toString().isEmpty())  lis = "Name"
        else if(binding.emailTextView.text.toString().isEmpty())    lis = "Email"
        else if(binding.passwordTextView.text.toString().isEmpty())     lis = "Password"
        else if(binding.confirmPasswordTextView.text.toString().isEmpty())      lis = "Confirm Password"
        else if(binding.confirmPasswordTextView.text.toString() != binding.passwordTextView.text.toString())
            lis = "The Password Confirmation does not match."
        return lis
    }

    fun userRegisteredSuccess(){
        //hideProgressDialog()
        startActivity(Intent(this, Drawer::class.java))
        finish()
    }

    private fun setUpActionBar() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setSupportActionBar(binding.customToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Sign Up"
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)
        binding.customToolBar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    fun showError(message: String){
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(
            ContextCompat.getColor(
                this@CreateUserActivity,
                R.color.snackbarcolor
            )
        )
        snackBar.show()
    }
    fun hideKeyboard(){
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if ( inputManager.isAcceptingText ){
            inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken,0)
        }
    }
}