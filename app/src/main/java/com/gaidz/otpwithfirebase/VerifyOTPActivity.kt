package com.gaidz.otpwithfirebase

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gaidz.otpwithfirebase.databinding.ActivityVerifyOTPBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class VerifyOTPActivity : AppCompatActivity() {

    private var binding: ActivityVerifyOTPBinding? = null

    private var forceResendingToken: PhoneAuthProvider.ForceResendingToken? = null
    private var verificationId: String? = null
    private var mobilePhone: String? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var phone: Phone

    companion object {
        const val TAG = "tes"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerifyOTPBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        firebaseAuth = FirebaseAuth.getInstance()

        setupOTPInputs()

        phone = intent.getParcelableExtra("phone")!!

        Log.d(TAG, "onCreates: ${phone.forceResendingToken} dan ${phone.mobileNumber}")

        mobilePhone = phone.mobileNumber
        verificationId = phone.verificationId
        forceResendingToken = phone.forceResendingToken

        binding?.tvMobile?.text = mobilePhone

        binding?.btnGetOtp?.setOnClickListener {
            if (binding?.rdtCode1?.text.toString().trim().isEmpty()
                || binding?.rdtCode2?.text.toString().trim().isEmpty()
                || binding?.rdtCode3?.text.toString().trim().isEmpty()
                || binding?.rdtCode4?.text.toString().trim().isEmpty()
                || binding?.rdtCode5?.text.toString().trim().isEmpty()
                || binding?.rdtCode6?.text.toString().trim().isEmpty()
            ) {
                Toast.makeText(this, "Please Enter Valid Code", Toast.LENGTH_SHORT).show()
            } else {
                val code = binding?.rdtCode1?.text.toString() +
                        binding?.rdtCode2?.text.toString() +
                        binding?.rdtCode3?.text.toString() +
                        binding?.rdtCode4?.text.toString() +
                        binding?.rdtCode5?.text.toString() +
                        binding?.rdtCode6?.text.toString()

                verifyNumberWithCode(verificationId, code)
            }
        }

        binding?.btnResend?.setOnClickListener {
            resendVerificationCodePhone(mobilePhone!!, forceResendingToken!!)
        }

        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(p0: FirebaseException) {
                binding?.progressBar?.visibility = View.GONE
                binding?.btnResend?.visibility = View.VISIBLE
                Toast.makeText(this@VerifyOTPActivity, "errornya ${p0.message}", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onCodeSent(
                newVerificationId: String,
                newForceResendingToken: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d(SendOTPActivity.TAG, "onCodeSent: $verificationId")
                verificationId = newVerificationId
                forceResendingToken = newForceResendingToken
            }
        }
    }

    private fun setupOTPInputs() {
        binding?.rdtCode1?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().trim().isNotEmpty()) {
                    binding?.rdtCode2?.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding?.rdtCode2?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().trim().isNotEmpty()) {
                    binding?.rdtCode3?.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding?.rdtCode3?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().trim().isNotEmpty()) {
                    binding?.rdtCode4?.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding?.rdtCode4?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().trim().isNotEmpty()) {
                    binding?.rdtCode5?.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding?.rdtCode5?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().trim().isNotEmpty()) {
                    binding?.rdtCode6?.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

    private fun resendVerificationCodePhone(
        phone: String,
        token: PhoneAuthProvider.ForceResendingToken
    ) {
        binding?.progressBar?.visibility = View.VISIBLE

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(mCallback)
            .setForceResendingToken(token)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyNumberWithCode(verificationId: String?, code: String) {
        binding?.progressBar?.visibility = View.VISIBLE

        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        binding?.progressBar?.visibility = View.VISIBLE

        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                binding?.progressBar?.visibility = View.GONE

                val phone = firebaseAuth.currentUser.phoneNumber
                Toast.makeText(this, "Logged In as $phone", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, ProfileActivity::class.java))
                finish()
            }

            .addOnFailureListener {
                binding?.progressBar?.visibility = View.GONE
                Toast.makeText(this, "hei error $it", Toast.LENGTH_SHORT).show()
            }
    }
}