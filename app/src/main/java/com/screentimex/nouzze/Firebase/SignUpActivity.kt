package com.screentimex.nouzze.Firebase

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.screentimex.nouzze.Activities.MainActivity
import com.screentimex.nouzze.R
import com.screentimex.nouzze.databinding.ActivityCreateUserBinding
import com.screentimex.nouzze.models.Constants
import com.screentimex.nouzze.models.UserDetails

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCreateUserBinding
    private lateinit var mFireStore: FirebaseFirestore
    private lateinit var mIntroDetails: Array<String>
    private lateinit var mUserName: String
    private var mUserAge: Int = 0
    private lateinit var mUserProfession: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra(Constants.INTRODETAILS)) {
            mIntroDetails = intent.getStringArrayExtra(Constants.INTRODETAILS)!!
            mUserName = mIntroDetails[0]
            mUserAge = mIntroDetails[1].toInt()
            mUserProfession = mIntroDetails[2]
        }

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
            mUserName = mUserName.trim{ it<=' '}
            val email = binding.emailTextView.text.toString().trim{ it<=' '}.toLowerCase()
            val password = binding.loginPasswordTextView.text.toString().trim{ it<=' '}
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        val user = UserDetails(firebaseUser.uid, email, mUserName, mUserAge, mUserProfession)
                        FireStoreClass().registerUser(this, user)
                    }
                    else{
                        //hideProgressDialog()
                        binding.progressBarButton.visibility = View.GONE
                        val errorMessage = task.exception!!.message!!.substringAfter("[").substringBefore("]")
                        showError(errorMessage)
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
        if(binding.emailTextView.text.toString().isEmpty())    lis = "Email address is required"
        else if(binding.loginPasswordTextView.text.toString().isEmpty())     lis = "Password is mandatory"
        else if(binding.confirmPasswordTextView.text.toString().isEmpty())      lis = "Please confirm your password"
        else if(binding.confirmPasswordTextView.text.toString() != binding.loginPasswordTextView.text.toString())
            lis = "Passwords do not match"
        return lis
    }

    fun userRegisteredSuccess(){
        //hideProgressDialog()
        Toast.makeText(this,"User Registered!!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    /*private fun setUpActionBar() {
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
    }*/

    fun showError(message: String){
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(
            ContextCompat.getColor(
                this@SignUpActivity,
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