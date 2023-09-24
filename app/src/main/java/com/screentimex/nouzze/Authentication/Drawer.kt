package com.screentimex.nouzze.Authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.screentimex.nouzze.Activities.ProfileActivity
import com.screentimex.nouzze.Activities.StoreActivity
import com.screentimex.nouzze.Adapters.UsageScreenRecyclerViewAdapter
import com.screentimex.nouzze.R
import com.screentimex.nouzze.databinding.ActivityDrawerBinding
import com.screentimex.nouzze.models.ProfileDetails
import com.screentimex.nouzze.models.ScreenUsageData

class Drawer : AppCompatActivity() {
    private lateinit var binding: ActivityDrawerBinding
    companion object {
        const val MY_PROFILE_REQ_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpActionBar()

        FireStoreClass().loadUserData(this@Drawer)

        binding.navDraawerHeaderInclude.logOutButton.setOnClickListener {
            signOut()
        }

        binding.navDraawerHeaderInclude.userImageNavHeader.setOnClickListener {
            val intent = Intent(this@Drawer, ProfileActivity::class.java)
            startActivityForResult(intent, MY_PROFILE_REQ_CODE)
        }
        val testingAdapter = arrayListOf<ScreenUsageData>(
            ScreenUsageData(R.drawable.appicon.toString(), "Whatsapp", "1 Hour 30 Min"),
            ScreenUsageData(R.drawable.appicon.toString(), "Instagram", "30 Min")
        )
        binding.includeAppBarLayout.MainScreenUsageActivity.mainScreenRecyclerView.layoutManager =
            LinearLayoutManager(this@Drawer)
        val mAdapter = UsageScreenRecyclerViewAdapter(this, testingAdapter)
        binding.includeAppBarLayout.MainScreenUsageActivity.mainScreenRecyclerView.adapter = mAdapter



    }

    private fun setUpActionBar(){
        setSupportActionBar(binding.includeAppBarLayout.toolbar)
        binding.includeAppBarLayout.toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24)
        binding.includeAppBarLayout.toolbar.setNavigationOnClickListener {
            toggleDrawer()
        }
    }

    private fun toggleDrawer(){
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        else{
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK && requestCode==MY_PROFILE_REQ_CODE){
            FireStoreClass().loadUserData(this)
            toggleDrawer()
        }
        else{
            Log.e("Cancelled","Cancelled")
        }
    }

    fun updateNavigationUserDetails(user: ProfileDetails){
        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_holder)
            .into(binding.navDraawerHeaderInclude.userImageNavHeader)
        binding.navDraawerHeaderInclude.userEmailNavHeader.text = user.email
        binding.navDraawerHeaderInclude.userNameNavHeader.text = user.name
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_shop_screen_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_icon -> {
//                Toast.makeText(this@Drawer, "Shop Working!!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this,StoreActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun signOut(){
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }


}