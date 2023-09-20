package com.screentimex.nouzze.Authentication

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.screentimex.nouzze.R
import com.screentimex.nouzze.databinding.ActivityCreateUserBinding

class CreateUserActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCreateUserBinding
    private var imageUrl : Uri? = null
    private lateinit var databaseReference : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createSpinner.visibility = View.INVISIBLE

        binding.createUserBtn.setOnClickListener {
            val userName = binding.createUserNameTxt.text.toString()
            val email = binding.createEmailTxt.text.toString()
            val password = binding.createPasswordTxt.text.toString()

            hideKeyboard()

            if (userName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() ) {
                enableSpinner(true)
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val firrebaseUser: FirebaseUser = task.result!!.user!!

                            Toast.makeText(
                                this,
                                "You are registered Successfully.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            enableSpinner(false)
                            Toast.makeText(
                                this,
                                task.exception!!.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    .addOnFailureListener {
                        enableSpinner(false)
                        Toast.makeText(this, "Error Occured", Toast.LENGTH_SHORT).show()
                    }

            } else {
                Toast.makeText(this, "All fields should be filled ", Toast.LENGTH_LONG).show()
                enableSpinner(false)
            }
        }
    }


    fun errorToast() {
        Toast.makeText(this, "Something went wrong try again", Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }

    fun enableSpinner(enable: Boolean ){
        val createuserbtn : Button = findViewById(R.id.createUserBtn)

        if ( enable ){
            binding.createSpinner.visibility = View.VISIBLE
        }else {
            binding.createSpinner.visibility = View.INVISIBLE
        }
        createuserbtn.isEnabled = !enable
    }

    fun getCurrentUUID() : String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if ( currentUser != null ){
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