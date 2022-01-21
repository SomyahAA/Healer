package com.example.healer.utils

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.healer.R
import com.example.healer.databinding.OtpBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit


open class OTPFragment : Fragment() {


    private lateinit var binding: OtpBinding
    private lateinit var forceResendingToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var mCallBacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private  var mVerificationId: String= ""
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var progressDialog : ProgressDialog
    private var verificationId: String? = null
    private val KEY_VERIFICATION_ID = "key_verification_id"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (verificationId == null && savedInstanceState != null) {
            mVerificationId = savedInstanceState.getString(KEY_VERIFICATION_ID)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = OtpBinding.inflate(layoutInflater)
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)


        binding.continueBTN.setOnClickListener {
            val code =binding.otpEnteredPin.text.toString().trim()

            if(TextUtils.isEmpty(code)){
                Toast.makeText(requireContext(), "Please enter the code", Toast.LENGTH_LONG).show()
            }else{
                startPhoneNumberVerification(R.id.psyPhoneNumber1.toString(),requireContext(),requireActivity())
                verifyingPhoneNumberWithCode(mVerificationId,code)
            }
        }
        binding.resendCodeTV.setOnClickListener {
            val phone =binding.otpEnteredPin.text.toString().trim()
            if(TextUtils.isEmpty(phone)){
                Toast.makeText(requireContext(), "Please enter the code", Toast.LENGTH_LONG).show()
            }else{
                resendVerificationCode(phone,forceResendingToken)
            }
        }

        return binding.root
    }

    private fun startPhoneNumberVerification(number:String,context: Context,activity: Activity) {
        Log.d(Constants.PSYCHOLOGISTREREGESTERTAG,"startPhoneNumberVerification :$number")
        progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setMessage("Verifying phone Number ... ")
        progressDialog.show()


        mCallBacks = object :PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                signInWithPhoneCredential(phoneAuthCredential)
                findNavController().navigate(R.id.action_OTPFragment_to_psyHomeFragment)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                progressDialog.dismiss()
                Log.d(Constants.PSYCHOLOGISTREREGESTERTAG,"onVerificationFailed : ${e.message}")


            }

            override fun onCodeSent(VerificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                Log.d(Constants.PSYCHOLOGISTREREGESTERTAG,"onCodeSent :$VerificationId")
                mVerificationId =VerificationId
                forceResendingToken =token
                progressDialog.dismiss()
            }

        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(mCallBacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun resendVerificationCode(phoneNumber: String,token: PhoneAuthProvider.ForceResendingToken){
        Log.d(Constants.PSYCHOLOGISTREREGESTERTAG,"resendVerificationCode :$phoneNumber")

        progressDialog.setMessage("Resending code ... ")
        progressDialog.show()

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(mCallBacks)
            .setForceResendingToken(token)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyingPhoneNumberWithCode(VerificationId :String,code:String){
        Log.d(Constants.PSYCHOLOGISTREREGESTERTAG,"verifyingPhoneNumberWithCode :$VerificationId ,$code")
        progressDialog.setMessage("Verifying code... ")
        progressDialog.show()

        val credential = PhoneAuthProvider.getCredential(VerificationId,code)
        auth.signInWithCredential(credential) .addOnSuccessListener {
        }
    }

    private fun signInWithPhoneCredential(credential: PhoneAuthCredential){
        Log.d(Constants.PSYCHOLOGISTREREGESTERTAG,"signInWithPhoneCredential :$credential ")

        progressDialog.setMessage("Logging In")

        auth.signInWithCredential(credential).addOnSuccessListener {
            progressDialog.dismiss()
            val phone = auth.currentUser?.phoneNumber
            Toast.makeText(requireContext(), "Logged in ", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {e->
            progressDialog.dismiss()
            Toast.makeText(requireContext(),"${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_VERIFICATION_ID, verificationId)
    }

}