package com.screentimex.nouzze.Authentication

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.screentimex.nouzze.Activities.MainActivity
import com.screentimex.nouzze.databinding.ActivityLoginBinding
import com.screentimex.nouzze.models.ProfileDetails

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private var imageUrl : Uri? = null
    private lateinit var dataBaseReference : DatabaseReference
    data class UserData(val name: String, val email: String, val img: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createSpinner.visibility = View.INVISIBLE

        binding.loginloginbtn.setOnClickListener {

            val email = binding.loginemailtxt.text.toString()
            val password = binding.loginpasswordtxt.text.toString()
            hideKeyboard()
            if ( email.isNotEmpty() && password.isNotEmpty() ) {
                enableSpinner(true)
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
//                            val firrebaseUser: FirebaseUser = task.result!!.user!!
                            Toast.makeText(
                                this,
                                "You are Logged In Please Wait",
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.loginemailtxt.text.clear()
                            binding.loginpasswordtxt.text.clear()
                            getData()
                        } else {
                            enableSpinner(false)
                            Toast.makeText(this, task.exception!!.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    .addOnFailureListener{
                        enableSpinner(false)
                        Toast.makeText(this,"Error Occured", Toast.LENGTH_SHORT).show()
                    }

            }else{
                Toast.makeText(this,"All fields should be filled ", Toast.LENGTH_LONG).show()
                enableSpinner(false)
            }
        }
        binding.loginnewuserbtn.setOnClickListener {
            val createUserIntent = Intent(this, CreateUserActivity::class.java)
            startActivity(createUserIntent)
        }

    }

    fun getData( ){

        val db = Firebase.firestore
        val docRef = db.collection("Login Credentials").document(getCurrentUUID())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val loggedInUser = document.toObject(ProfileDetails::class.java)!!

                    val name = loggedInUser.userName?:"UserName"
                    val email = loggedInUser.email?:"Email"


                    enableSpinner(false)
                    val mainIntent = Intent(this, MainActivity::class.java)


                    val bundle = Bundle()
                    bundle.putString("name",name)
                    bundle.putString("email",email)
                    bundle.putBoolean("IsLog",true)


                    mainIntent.putExtras(bundle)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(mainIntent)
                } else {
                    Toast.makeText(this,"No such document", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this,"Get failed with " + " "+ exception, Toast.LENGTH_SHORT).show()
            }
    }

    fun enableSpinner(enable: Boolean ){

        if ( enable ){
            binding.createSpinner.visibility = View.VISIBLE
        }else{
            binding.createSpinner.visibility = View.INVISIBLE
        }
        binding.loginnewuserbtn.isEnabled = !enable
        binding.loginloginbtn.isEnabled = !enable
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
        val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        if ( inputManager.isAcceptingText ){
            inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken,0)
        }
    }
}