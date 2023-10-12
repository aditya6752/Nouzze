package com.screentimex.nouzze.Firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.screentimex.nouzze.Activities.AddAddress
import com.screentimex.nouzze.Activities.Address
import com.screentimex.nouzze.Activities.CheckOut
import com.screentimex.nouzze.Activities.MainActivity
import com.screentimex.nouzze.Activities.ProfileActivity
import com.screentimex.nouzze.models.AddressDetails
import com.screentimex.nouzze.models.Constants
import com.screentimex.nouzze.models.UserDetails
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FireStoreClass: AppCompatActivity() {
    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUpActivity, userInfo: UserDetails){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUUID())
            .set(userInfo, SetOptions.merge()).addOnSuccessListener {
                Log.e("MyTag", "Register")
                activity.userRegisteredSuccess()
            }.addOnFailureListener {
                Log.e("FireStoreClassSignUp", it.message!!)
                activity.showError(it.message!!)
            }
    }

    fun sendEmailVerificationLink(activity: Activity, user: FirebaseUser) {
        user.sendEmailVerification()
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    if(activity is MainActivity) {
                        activity.emailVerificationLinkSendSuccessfully()
                    }
                } else {
                    if(activity is MainActivity) {
                        activity.emailVerificationLinkSendFailed()
                    }
                }
            }.addOnFailureListener {
                if(activity is MainActivity) {
                    activity.showSnackBar(it.message!!)
                }
                Log.e("FireStoreClassSignUp", it.message!!)
            }
    }

    fun loadUserData(activity: Activity){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUUID())
            .get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(UserDetails::class.java)
                if(loggedInUser!=null){
                    when(activity){
                        is MainActivity -> {
                            Log.i("YourTag", "Check")
                            activity.updateNavigationUserDetails(loggedInUser)
                        }
                        is ProfileActivity -> {
                            activity.getUserDataForProfile(loggedInUser)
                        }
                        is CheckOut -> {
                            activity.getUserData(loggedInUser)
                        }
                    }
                }
            }.addOnFailureListener {
                when(activity){
                    is MainActivity -> {
                        Log.i("YourTag", "Check")
                        activity.showSnackBar(it.message!!)
                    }
                    is ProfileActivity -> {
                        activity.showSnackBar(it.message!!)
                    }
                    is CheckOut -> {
                        activity.showSnackBar(it.message!!)
                    }
                }
                Log.e("FireStoreClassSignUp", it.message!!)
            }
    }

    fun getUserDataMidNight(activity: Activity){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUUID())
            .get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(UserDetails::class.java)
                if(loggedInUser!=null){
                    when(activity){
                        is MainActivity -> {
                            Log.i("YourTag", "Check")
                            activity.updateNavigationUserDetails(loggedInUser)
                        }
                    }
                }
            }.addOnFailureListener {
                when(activity){
                    is MainActivity -> {
                        Log.i("YourTag", "Check")
                        activity.showSnackBar(it.message!!)
                    }
                }
                Log.e("FireStoreClassSignUp", it.message!!)
            }
    }

    fun updateProfileData(activity: Activity,
                          userHashMap: HashMap<String, Any>){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUUID())
            .update(userHashMap)
            .addOnSuccessListener {
                if(activity is ProfileActivity) {
                    activity.profileUpdateSuccess()
                }
                if(activity is CheckOut) {
                    activity.updateProfileData()
                }
            }.addOnFailureListener{
                if(activity is ProfileActivity) {
                    activity.showSnackBar(it.message!!)
                }
                if(activity is CheckOut) {
                    activity.showSnackBar(it.message!!)
                }
                Log.e("FireStoreClassSignUp", it.message!!)
            }
    }

    fun addOrUpdateAddress(activity: AddAddress, addressDetails: AddressDetails) {
        mFireStore.collection(Constants.ADDRESS)
            .document(getCurrentUUID())
            .set(addressDetails, SetOptions.merge())
            .addOnSuccessListener {
                activity.addressAddedUpdateSuccessfully()
            }.addOnFailureListener {
                Toast.makeText(activity, it.toString(), Toast.LENGTH_LONG).show()
                activity.showSnackBar(it.message!!)
            }
    }

    fun getAddress(activity: Activity) {
        mFireStore.collection(Constants.ADDRESS)
            .document(getCurrentUUID())
            .get()
            .addOnSuccessListener { document ->
                if(document.exists()) {
                    val address = document.toObject(AddressDetails::class.java)
                    if(activity is Address) {
                        activity.getAddressFromDatabase(address!!)
                    }
                    else if(activity is AddAddress) {
                        activity.populateActivity(address!!)
                    }else if ( activity is CheckOut ){
                        activity.populateAdress(address!!)
                    }
                }
                else {
                    if(activity is Address) {
                        activity.noAddressSaved()
                    }
                    else if(activity is AddAddress) {
                        activity.noAddressInDatabase()
                    }
                }
            }.addOnFailureListener {
                if(activity is Address) {
                    activity.showSnackBar(it.message!!)
                }
                else if(activity is AddAddress) {
                    activity.showSnackBar(it.message!!)
                }else if ( activity is CheckOut ){
                    activity.showSnackBar(it.message!!)
                }
                Toast.makeText(activity, it.toString(), Toast.LENGTH_LONG).show()
            }
    }


    fun getCurrentUUID(): String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if(currentUser != null) {
            currentUserId = currentUser.uid
        }
        return currentUserId
    }
}