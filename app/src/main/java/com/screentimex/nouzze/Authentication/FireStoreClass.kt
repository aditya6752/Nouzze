package com.screentimex.nouzze.Authentication

import android.app.Activity
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.screentimex.nouzze.Activities.Drawer
import com.screentimex.nouzze.Activities.ProfileActivity
import com.screentimex.nouzze.models.Constants
import com.screentimex.nouzze.models.ProfileDetails

class FireStoreClass: AppCompatActivity() {
    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: CreateUserActivity, userInfo: ProfileDetails){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUUID())
            .set(userInfo, SetOptions.merge()).addOnSuccessListener {
                Log.e("MyTag", "Register")
                activity.userRegisteredSuccess()
            }.addOnFailureListener {
                    _ ->
                Log.e("FireStoreClassSignUp","Error")
            }
    }

    fun loadUserData(activity: Activity){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUUID())
            .get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(ProfileDetails::class.java)
                if(loggedInUser!=null){
                    when(activity){
                        is Drawer -> {
                            Log.i("YourTag", "Check")
                            activity.updateNavigationUserDetails(loggedInUser)
                        }
                        is ProfileActivity -> {
                            activity.getUserDataForProfile(loggedInUser)
                        }
                    }
                }
            }.addOnFailureListener {
                    _ ->
                Log.e("FireStoreClassSignUp","Error")
            }
    }

    fun updateProfileData(activity: ProfileActivity,
                          userHashMap: HashMap<String, Any>){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUUID())
            .update(userHashMap)
            .addOnSuccessListener {
                Log.e(activity.javaClass.simpleName, "nobro")
                activity.profileUpdateSuccess()
            }.addOnFailureListener{
                    e ->
                //activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,"dataanayltic", e)
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