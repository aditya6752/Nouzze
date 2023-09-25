package com.screentimex.nouzze.Activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.screentimex.nouzze.Firebase.FireStoreClass
import com.screentimex.nouzze.R
import com.screentimex.nouzze.databinding.ActivityProfileBinding
import com.screentimex.nouzze.models.Constants
import com.screentimex.nouzze.models.ProfileDetails

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private var selectedImageUri: Uri? = null
    private var profileImageUri: String = ""
    private lateinit var userDetails: ProfileDetails
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpActionBar()

        FireStoreClass().loadUserData(this)

        binding.profileImage.setOnClickListener {
            val intent = Intent("android.intent.action.GET_CONTENT")
            intent.type = "image/*"
            launchGalleryActivity.launch(intent)
        }

        binding.updateButton.setOnClickListener {
            binding.progressBarButton.visibility = View.VISIBLE
            binding.updateTextViewOnButton.visibility = View.GONE
            if(selectedImageUri != null) {
                uploadUserImage()
            }  else{
                updateProfileData()
            }
        }
    }

    private fun setUpActionBar() {
        setSupportActionBar(binding.customToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "My Profile"
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)
        binding.customToolBar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private val launchGalleryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK) {
            selectedImageUri = it.data!!.data
            selectedImageUri?.let { imageData ->
                setImageToProfile(imageData)
            }
        }
    }

    private fun uploadUserImage(){
        //showCustomProgressDialog("Please Wait")
        if(selectedImageUri != null){
            val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
                "User Image" + System.currentTimeMillis()
                        + "." + Constants.getFileExtension(this, selectedImageUri!!))
            sRef.putFile(selectedImageUri!!).addOnSuccessListener {
                    taskSnapshot ->
                Log.e(
                    "binod",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )
                taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        uri ->
                    Log.e("Downloadable Image URL", uri.toString())
                    profileImageUri = uri.toString()
                    updateProfileData()
                }
            }.addOnFailureListener{
                    exception ->
                Toast.makeText(this@ProfileActivity, "${exception.message}", Toast.LENGTH_SHORT).show()
                //hideProgressDialog()
            }
        }
    }

    private fun updateProfileData(){
        val userHashMap = HashMap<String, Any>()
        var isChanged = false
        if(profileImageUri.isNotEmpty() && profileImageUri!= userDetails.image){
            userHashMap[Constants.IMAGE] =
                profileImageUri
            isChanged = true
        }
        if(binding.profileNameTextView.text.toString() != userDetails.name){
            userHashMap[Constants.NAME] =
                binding.profileNameTextView.text.toString()
            isChanged = true
        }
        if(!binding.profilePhoneNumberTextView.text.toString().isBlank()
            && binding.profilePhoneNumberTextView.text.toString() != userDetails.phoneNumber.toString()){
            userHashMap[Constants.PHONENUMBER] =
                binding.profilePhoneNumberTextView.text.toString().toLong()
            isChanged = true
        }
        // If there is a change in current data
        if(isChanged)
            FireStoreClass().updateProfileData(this, userHashMap)
        // else we will end the activity
        else {
            //hideProgressDialog()
            finish()
        }
    }

    fun profileUpdateSuccess(){
        //hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()
    }
    private fun setImageToProfile(uri: Uri){
        Glide
            .with(this)
            .load(uri)
            .centerCrop()
            .placeholder(R.drawable.ic_user_holder)
            .into(binding.profileImage)
    }

    fun getUserDataForProfile(user: ProfileDetails){
        userDetails = user
        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_holder)
            .into(binding.profileImage)
        binding.profileNameTextView.setText(user.name)
        binding.profileEmailTextView.setText(user.email)
        if(user.phoneNumber != 0L){
            binding.profilePhoneNumberTextView.setText(user.phoneNumber.toString())
        }
    }
}