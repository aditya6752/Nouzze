package com.screentimex.nouzze.Authentication

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.screentimex.nouzze.Activities.ProfileActivity
import com.screentimex.nouzze.R
import com.screentimex.nouzze.databinding.ActivityDrawerBinding
import com.screentimex.nouzze.databinding.NavHeaderDrawerBinding

class Drawer : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityDrawerBinding
    private var imageUrl : Uri? = null
    private lateinit var databaseReference : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val Intent = intent
        val data = Intent.extras

        val email = data?.getString("email")
        val name = data?.getString("name")
        val isLoggedIn = data?.getBoolean("IsLog")?:false

        setSupportActionBar(binding.appBarDrawer.toolbar)

        val loginbtn : Button = findViewById(R.id.loginButtonNavHeader)


        if ( isLoggedIn ) {
            binding.navDraawerHeaderInclude.userNameNavHeader.text = name
            binding.navDraawerHeaderInclude.userEmailNavHeader.text = email
            binding.navDraawerHeaderInclude.loginButtonNavHeader.text = "Log Out"

        }else{
            binding.navDraawerHeaderInclude.userNameNavHeader.text = "User Name"
            binding.navDraawerHeaderInclude.userEmailNavHeader.text = "Email"
            binding.navDraawerHeaderInclude.loginButtonNavHeader.text = "Log In"
        }


        loginbtn.setOnClickListener {
            if (  !isLoggedIn ) {
                val loginIntent = Intent(this, LoginActivity::class.java)
                startActivity(loginIntent)
            }else {
                signOut()
            }
        }

        binding.navDraawerHeaderInclude.userImageNavHeader.setOnClickListener {
            val intent = Intent(this@Drawer, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
//    }



    fun signOut(){
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, Drawer::class.java))
        finish()

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