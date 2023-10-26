package com.screentimex.nouzze.Firebase

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.screentimex.nouzze.Activities.IntroActivity
import com.screentimex.nouzze.Activities.MainActivity
import com.screentimex.nouzze.R
import com.screentimex.nouzze.databinding.ActivityLoginBinding

class SignInActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if ( !isInternetConnected(this) ){
            showSnackBar("No Internet !!")
        }

        binding.loginButton.setOnClickListener {
            hideKeyboard()
            if ( isInternetConnected(this) ) {
                binding.progressBarButton.visibility = View.VISIBLE
                signInAuth()
            }
        }
        binding.signUpButton.setOnClickListener {
            hideKeyboard()
            if ( isInternetConnected(this) ) {
                val intent = Intent(this@SignInActivity, IntroActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun signInAuth(){
        val okCredentials = validateForm()
        if(okCredentials.isBlank()){
            val email = binding.loginEmailTextView.text.toString().trim{ it<=' '}.toLowerCase()
            val password = binding.loginPasswordTextView.text.toString().trim{ it<=' '}
            //showCustomProgressDialog(resources.getString(R.string.please_wait))

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            signInSuccess()
                        } else {
                            //hideProgressDialog()
                            binding.progressBarButton.visibility = View.GONE
                            val errorMessage =
                                task.exception!!.message!!.substringAfter("[").substringBefore("]")
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
        var lis = ""
        if(binding.loginEmailTextView.text.toString().isEmpty())    lis = "Email address is required"
        else if(binding.loginPasswordTextView.text.toString().isEmpty())     lis = "Password is mandatory"
        return lis
    }

    private fun signInSuccess(){
        binding.progressBarButton.visibility = View.GONE
        val mUser = FirebaseAuth.getInstance().currentUser!!
        if(mUser.isEmailVerified) {
            startActivity(Intent(this@SignInActivity, MainActivity::class.java))
        } else {
            FireStoreClass().sendEmailVerificationLink(this, mUser)
        }
    }

    fun emailVerificationLinkSentSuccess() {
        Toast.makeText(this, "Email Verification Link Sent. Please Verify and Sign In", Toast.LENGTH_LONG).show()
    }
    fun emailVerificationLinkSentFail(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showError(message: String){
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(
            ContextCompat.getColor(
                this@SignInActivity,
                R.color.snackBarColor
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
    fun showSnackBar(message: String){
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(
            ContextCompat.getColor(
                this@SignInActivity,
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