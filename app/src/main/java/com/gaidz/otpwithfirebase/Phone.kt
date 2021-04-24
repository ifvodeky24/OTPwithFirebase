package com.gaidz.otpwithfirebase

import android.os.Parcelable
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.parcelize.Parcelize

@Parcelize
data class Phone(
    val forceResendingToken: PhoneAuthProvider.ForceResendingToken,
    val mobileNumber : String,
    val verificationId : String
) : Parcelable