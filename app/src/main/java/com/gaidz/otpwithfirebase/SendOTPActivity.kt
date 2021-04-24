package com.gaidz.otpwithfirebase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gaidz.otpwithfirebase.databinding.ActivitySendOTPBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class SendOTPActivity : AppCompatActivity() {

    private var binding: ActivitySendOTPBinding? = null

    private lateinit var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var mVerificationId: String? = null
    private lateinit var firebaseAuth: FirebaseAuth

    companion object {
        const val TAG = "main"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySendOTPBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding?.btnSend?.setOnClickListener {
            binding?.let {
                if (it.edtMobileNumber.text.toString().isEmpty()) {
                    Toast.makeText(this, "Enter Moblle Number", Toast.LENGTH_SHORT).show()
                } else {
                    startPhoneNumberVerification("+62${it.edtMobileNumber.text.toString()}")

                }
            }

        }

        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(p0: FirebaseException) {
                binding?.progressBar?.visibility = View.GONE
                binding?.btnSend?.visibility = View.VISIBLE
                Toast.makeText(this@SendOTPActivity, "errornya ${p0.message}", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onCodeSent(
                verificationId: String,
                forceResendingToken: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d(TAG, "onCodeSent: $verificationId")
                val arg = Bundle()
                arg.putParcelable(
                    "phone", Phone(
                        forceResendingToken,
                        "+62${binding?.edtMobileNumber?.text.toString()}",
                        verificationId
                    )
                )

                val intent = Intent(this@SendOTPActivity, VerifyOTPActivity::class.java)
                intent.putExtras(arg)
                startActivity(intent)
            }
        }


    }

    private fun startPhoneNumberVerification(phone: String) {
        binding?.progressBar?.visibility = View.VISIBLE
        binding?.btnSend?.visibility = View.GONE

        Log.d(TAG, "startPhoneNumberVerification: $phone")

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(mCallback)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}