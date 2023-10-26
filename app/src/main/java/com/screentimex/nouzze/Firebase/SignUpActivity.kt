package com.screentimex.nouzze.Firebase

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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
import com.screentimex.nouzze.Services.MidNightUsageStateSharedPref
import com.screentimex.nouzze.databinding.ActivityCreateUserBinding
import com.screentimex.nouzze.models.Constants
import com.screentimex.nouzze.models.UserDetails

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCreateUserBinding
    private lateinit var mIntroDetails: Array<String>
    private lateinit var mUserName: String
    private var mUserAge: Int = 0
    private lateinit var mUserProfession: String
    private lateinit var mSharedPref: MidNightUsageStateSharedPref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mSharedPref = MidNightUsageStateSharedPref(this@SignUpActivity)
        if(intent.hasExtra(Constants.INTRODETAILS)) {
            mIntroDetails = intent.getStringArrayExtra(Constants.INTRODETAILS)!!
            mUserName = mIntroDetails[0]
            mUserAge = mIntroDetails[1].toInt()
            mUserProfession = mIntroDetails[2]
        }
        if ( !isInternetConnected(this) ){
            showSnackBar("No Internet !!")
        }
        binding.signUpButton.setOnClickListener {
            hideKeyboard()
            if ( isInternetConnected(this) ) {
                binding.progressBarButton.visibility = View.VISIBLE
                registerUser()
            }
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
                        //mSharedPref.saveDataObject(Constants.MID_NIGHT_USER_DATA, user)
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

    fun userRegisteredSuccess(user: FirebaseUser){
        FireStoreClass().sendEmailVerificationLink(this, user)
    }

    fun showError(message: String){
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(
            ContextCompat.getColor(
                this@SignUpActivity,
                R.color.snackBarColor
            )
        )
        snackBar.show()
    }
    private fun hideKeyboard(){
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
                this@SignUpActivity,
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

//    private fun checkEmailVerifiedOrNot() {
//        mUser.reload().addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                mUser = FirebaseAuth.getInstance().currentUser!! // Refresh the user object
//
//                if (mUser.isEmailVerified) {
//                    // User's email is verified
//                    binding.apply {
//                        includeAppBarLayout.MainScreenUsageActivity.verifyEmailButton.visibility = View.GONE
//                        includeAppBarLayout.MainScreenUsageActivity.permissionTextView.visibility = View.VISIBLE
//                        includeAppBarLayout.MainScreenUsageActivity.givePermissionButton.visibility = View.VISIBLE
//                    }
//                } else {
//                    // User's email is not verified
//                    binding.apply {
//                        includeAppBarLayout.MainScreenUsageActivity.mainScreenRecyclerView.visibility = View.INVISIBLE
//                        includeAppBarLayout.MainScreenUsageActivity.verifyEmailButton.visibility = View.VISIBLE
//                        includeAppBarLayout.MainScreenUsageActivity.marketPlaceButton.isEnabled = false
//                    }
//                }
//            } else {
//                // Error occurred while reloading user data
//                // Handle the error, check task.exception for details
//                val errorMessage = task.exception?.message ?: "Unknown error"
//                Log.e("EmailVerification", "Error: $errorMessage")
//            }
//        }
//    }
    fun emailVerificationLinkSendSuccessfully() {
        binding.apply {
            emailTextViewLayout.visibility = View.GONE
            loginPasswordTextViewLayout.visibility = View.GONE
            confirmPasswordTextViewLayout.visibility = View.GONE
            signUpButton.visibility = View.GONE
            progressBarButton.visibility = View.GONE
            emailVerificationLinkText.visibility = View.VISIBLE
        }
    }
    fun emailVerificationLinkSendFailed() {
        Toast.makeText(this@SignUpActivity, "Network Issue!!", Toast.LENGTH_LONG).show()
    }

}