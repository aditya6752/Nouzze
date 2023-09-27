package com.screentimex.nouzze.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import com.screentimex.nouzze.Firebase.FireStoreClass
import com.screentimex.nouzze.R
import com.screentimex.nouzze.Services.PreferenceManager
import com.screentimex.nouzze.databinding.ActivityCheckOutBinding
import com.screentimex.nouzze.models.AddressDetails
import com.screentimex.nouzze.models.Constants
import com.screentimex.nouzze.models.ProductDetails
import com.screentimex.nouzze.models.ProfileDetails
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
    lateinit var mUserDetails: ProfileDetails
    lateinit var preferenceManagerCheckOut : PreferenceManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckOutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpActionBar()

        preferenceManagerCheckOut = PreferenceManager(this)
        productDetails = preferenceManagerCheckOut.getDataObject(Constants.PRODUCTDETAILS)!!

        FireStoreClass().getAddress(this)
        FireStoreClass().loadUserData(this)

        binding.productName.text = productDetails.productName
        binding.productPrice.text = productDetails.productPrice
        val imgId = this.resources.getIdentifier(productDetails.productImg,"drawable",this.packageName)
        binding.productImg.setImageResource(imgId)

        binding.BuyNowButton.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                sendEmail()
            }
        }

    }
    fun populateAdress(addressDetails : AddressDetails ){
        mAddressDetails = addressDetails
        binding.UserName.setText("Hi , ${addressDetails.Name}")
        binding.UserMobileNumber.setText(addressDetails.Mobile_Number)
        binding.UserAreaFlatNumber.setText("${addressDetails.Flat_Number} / ${addressDetails.Area} / ${addressDetails.Landmark} ")
        binding.UserCityStatePin.setText("${addressDetails.City} / ${addressDetails.State} / ${addressDetails.Pincode}")
    }
    private fun setUpActionBar() {
        setSupportActionBar(binding.customToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Order Review"
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)
        binding.customToolBar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
    fun getUserData(UserData : ProfileDetails ){
        mUserDetails = UserData
        binding.userNameWelcome.text = mUserDetails.name
    }

    // This for GMAIL SMTP

    fun sendEmail() {
        val username = "screentimex@gmail.com"
        val password = "gmlm tdwq ayrd xjqd"

        val messageToBeSend = makeString()

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
                InternetAddress.parse("ritikrawat2448@gmail.com, screentimex@gmail.com" ))
            message.subject = "Confirmation of Order "
            message.setText(messageToBeSend)

            Transport.send(message)

            println("Email sent successfully.")

        } catch (e: MessagingException) {
            throw RuntimeException(e)
        }
    }

    fun makeString() : String {
        val response : String = "Hi ${mUserDetails.name} , Your order is placed . You ordered a ${productDetails.productName} which has a" +
                " value of ${productDetails.productPrice} points . After this order , you've left with 10 points . " +
                " The Order will be shipped to ${mAddressDetails.Flat_Number} / ${mAddressDetails.Area} , ${mAddressDetails.Landmark} , " +
                "${mAddressDetails.City} , ${mAddressDetails.State} , ${mAddressDetails.Pincode} and your Mobile Number is " +
                "${mAddressDetails.Mobile_Number} ."
        return response
    }

}