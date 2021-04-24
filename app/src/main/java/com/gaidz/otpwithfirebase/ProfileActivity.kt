package com.gaidz.otpwithfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gaidz.otpwithfirebase.databinding.ActivityProfileBinding
import com.gaidz.otpwithfirebase.databinding.ActivitySendOTPBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {

    private var binding: ActivityProfileBinding? = null

    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        firebaseAuth = FirebaseAuth.getInstance()

        checkUser()

        binding?.btnSignOut?.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }
    }

    private fun checkUser() {

        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null){
            startActivity(Intent(this, SendOTPActivity::class.java))
            finish()
        } else {
            val phone = firebaseUser.phoneNumber
            binding?.tvPhone?.text = phone
        }
    }
}