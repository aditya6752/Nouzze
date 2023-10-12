package com.screentimex.nouzze.Activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.screentimex.nouzze.Firebase.FireStoreClass
import com.screentimex.nouzze.R
import com.screentimex.nouzze.Services.MidNightUsageStateSharedPref
import com.screentimex.nouzze.Services.ProductDetailSharedPref
import com.screentimex.nouzze.databinding.ActivityCheckOutBinding
import com.screentimex.nouzze.models.AddressDetails
import com.screentimex.nouzze.models.Constants
import com.screentimex.nouzze.models.ProductDetails
import com.screentimex.nouzze.models.UserDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Properties
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class CheckOut : AppCompatActivity() {

    lateinit var productDetails : ProductDetails
    lateinit var binding : ActivityCheckOutBinding
    lateinit var mAddressDetails : AddressDetails
    lateinit var mUserDetails: UserDetails
    lateinit var productDetailSharedPrefCheckOut : ProductDetailSharedPref
    private lateinit var mSharedPrefMidNightUserDetails: MidNightUsageStateSharedPref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckOutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpActionBar()

        productDetailSharedPrefCheckOut = ProductDetailSharedPref(this)
        productDetails = productDetailSharedPrefCheckOut.getDataObject(Constants.PRODUCTDETAILS)!!

        mSharedPrefMidNightUserDetails = MidNightUsageStateSharedPref(this@CheckOut)

        FireStoreClass().getAddress(this)
        mUserDetails = mSharedPrefMidNightUserDetails.getDataObject(Constants.MID_NIGHT_USER_DATA)
        binding.userNameWelcome.setText("Hi ${mUserDetails.name} , Your Order  ")
        binding.balance.text = "My Points: " + mUserDetails.points.toString()

        binding.productName.text = productDetails.productName
        binding.productPrice.text = "Product Price: " + productDetails.productPrice
        val imgId = this.resources.getIdentifier(productDetails.productImg,"drawable",this.packageName)
        binding.productImg.setImageResource(imgId)

        binding.BuyNowButton.setOnClickListener {
            val balance = mUserDetails.points - productDetails.productPrice.toLong()
            if ( !isInternetConnected() ){
                showSnackBar("No Internet !!")
            } else if(balance > 0 ) {
                GlobalScope.launch(Dispatchers.IO) {
                    sendEmail()
                }
                val userHashMap = HashMap<String, Any>()
                userHashMap[Constants.POINTS] = balance
                FireStoreClass().updateProfileData(this@CheckOut, userHashMap)
            } else {
                showSnackBar("Insufficient Balance!!")

            }
        }

    }
    fun populateAdress(addressDetails : AddressDetails ){
        mAddressDetails = addressDetails
        binding.UserName.text = addressDetails.Name
        binding.UserMobileNumber.setText("Mobile Number : ${addressDetails.Mobile_Number}")
        binding.UserAreaFlatNumber.setText("${addressDetails.Flat_Number} / ${addressDetails.Area} / ${addressDetails.Landmark} ")
        binding.UserCityStatePin.setText("${addressDetails.City} / ${addressDetails.State} / ${addressDetails.Pincode}")
    }
    private fun setUpActionBar() {
        setSupportActionBar(binding.customToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Order Review"
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)
        binding.customToolBar.setNavigationOnClickListener {
            finish()
        }
    }

    fun updateProfileData(){
        startActivity(Intent(this@CheckOut, FinalActivity::class.java))
    }
    fun getUserData(UserData : UserDetails){
        mUserDetails = UserData
        binding.userNameWelcome.setText("Hi ${mUserDetails.name} , Your Order  ")
        binding.balance.text = "Balance: " + mUserDetails.points.toString()
    }

    // This for GMAIL SMTP

    fun sendEmail() {
        val username = "screentimex@gmail.com"
        val password = "gmlm tdwq ayrd xjqd"

        val messageToBeSend = mEmailMessage()

        val props = Properties()
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "true"
        props["mail.smtp.host"] = "smtp.gmail.com"
        props["mail.smtp.port"] = "587"

        val session: Session = Session.getInstance(props, object : javax.mail.Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(username, password)
            }
        })
        try {
            val message = MimeMessage(session)
            message.setFrom(InternetAddress(username))
            message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse("${mUserDetails.email}, screentimex@gmail.com" ))
            message.subject = "Confirmation of Order "
            message.setText(messageToBeSend)

            Transport.send(message)

            println("Email sent successfully.")

        } catch (e: MessagingException) {
            throw RuntimeException(e)
        }
    }

    private fun mEmailMessage() : String {
        val response : String = "Hi ${mUserDetails.name} ,\nYour order is placed.\nYou ordered a ${productDetails.productName} which has a" +
                "value of ${productDetails.productPrice} points. After this order , you've left with ${mUserDetails.points - productDetails.productPrice.toLong()} points . \n" +
                "Your Order will be shipped to ${mAddressDetails.Flat_Number} / ${mAddressDetails.Area} , ${mAddressDetails.Landmark} , " +
                "${mAddressDetails.City} , ${mAddressDetails.State} , ${mAddressDetails.Pincode} and your Mobile Number is" +
                "${mAddressDetails.Mobile_Number} .\nThank You \nTeam Nouzze"
        return response
    }

    fun showSnackBar(message: String){
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(
            ContextCompat.getColor(
                this@CheckOut,
                R.color.snackBarColor
            )
        )
        snackBar.show()
    }
    private fun isInternetConnected(): Boolean {
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}