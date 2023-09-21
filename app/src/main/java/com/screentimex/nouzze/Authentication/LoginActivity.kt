package com.screentimex.nouzze.Authentication

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.screentimex.nouzze.Activities.MainActivity
import com.screentimex.nouzze.Activities.ProfileActivity
import com.screentimex.nouzze.R
import com.screentimex.nouzze.databinding.ActivityCreateUserBinding
import com.screentimex.nouzze.databinding.ActivityLoginBinding
import com.screentimex.nouzze.models.ProfileDetails

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpActionBar()

        binding.loginButton.setOnClickListener {
            binding.progressBarButton.visibility = View.VISIBLE
            signInAuth()
        }
        binding.signUpButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, CreateUserActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signInAuth(){
        val okCredentials = validateForm()
        if(okCredentials.isBlank()){
            val email = binding.loginEmailTextView.text.toString().trim{ it<=' '}.toLowerCase()
            val password = binding.loginPasswordTextView.text.toString().trim{ it<=' '}
            //showCustomProgressDialog(resources.getString(R.string.please_wait))
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    signInSuccess()
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
        if(binding.loginEmailTextView.text.toString().isEmpty())    lis = "Email"
        else if(binding.loginPasswordTextView.text.toString().isEmpty())     lis = "Password"
        return lis
    }

    fun signInSuccess(){
        //hideProgressDialog()
        binding.progressBarButton.visibility = View.GONE
        Toast.makeText(this@LoginActivity, ":Login Success!!",Toast.LENGTH_SHORT).show()
        startActivity(Intent(this@LoginActivity, Drawer::class.java))
        finish()
    }
    private fun setUpActionBar() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setSupportActionBar(binding.customToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Sign In"
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
                this@LoginActivity,
                R.color.snackbarcolor
            )
        )
        snackBar.show()
    }
}