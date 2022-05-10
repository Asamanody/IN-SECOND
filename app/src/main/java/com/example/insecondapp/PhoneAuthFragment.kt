package com.example.insecondapp

import com.example.insecondapp.repos.FirbaseRepo.Companion.firbaseRepoInstance
import androidx.navigation.Navigation.findNavController

import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.auth.FirebaseUser
import com.example.insecondapp.storage.SeesionManager
import com.google.firebase.database.DatabaseReference
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import com.google.android.gms.tasks.OnCompleteListener
import android.util.Log
import com.example.insecondapp.PhoneAuthFragment
import com.hbb20.CountryCodePicker.OnCountryChangeListener
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import android.widget.Toast
import com.google.firebase.database.DatabaseError
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import android.text.TextUtils
import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import com.example.insecondapp.databinding.FragmentPhoneAuthBinding
import com.example.insecondapp.models.User
import java.util.concurrent.TimeUnit

class PhoneAuthFragment : Fragment() {
    lateinit var phoneAuthBinding: FragmentPhoneAuthBinding
    var selected_country_code = "+91"
    var phoneNumber = "999999999"
    var codeByUSer: String? = null

    /////////////////firbase phone auth /////////////////
    private var mVerificationInProgress = false // لو الفيريفاى شغال
    private var mVerificationId: String? = null
    private lateinit var mResendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    /////user in server////
    private lateinit var user: FirebaseUser
    private lateinit var userId: String
    private lateinit var userCreated: String

    ////session manager /////
    lateinit var seesionManager: SeesionManager

    //////database refrence////
    lateinit var databaseReference: DatabaseReference

    ////fcm/////
    var deviceToken: String? = null


    // user data if already register
    lateinit var userData:User

    //user data first to register
    lateinit var firstUserData :User

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        phoneAuthBinding = FragmentPhoneAuthBinding.inflate(inflater, container, false)
        return phoneAuthBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState?.let { onViewStateRestored(it) }

        //////seesionManager//////
        seesionManager = SeesionManager(activity!!)

        ///////users root
        databaseReference = firbaseRepoInstance.database.getReference("users")

        ///////////get device token//////
        firbaseRepoInstance!!.firebaseMessaging.token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            deviceToken = task.result
        })

        phoneAuthBinding!!.countryCodePicker.setOnCountryChangeListener {
            //set the country code of the user
            selected_country_code = phoneAuthBinding!!.countryCodePicker.selectedCountryCodeWithPlus
        }
        phoneAuthBinding!!.btnContinue.setOnClickListener(View.OnClickListener {
            phoneNumber = selected_country_code + phoneAuthBinding!!.phoneNumEdt.text.toString()
                .trim { it <= ' ' }
            if (!validatePhoneNumber()) {
                return@OnClickListener
            }

            ///check user in the database return true and return data
            val check_user = databaseReference!!.orderByChild("phone_number").equalTo(phoneNumber)

            check_user.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {


                        //user already registered

                        //get all data
                        val deviceToken1=
                            snapshot.child(phoneNumber).child("device_token").getValue(
                                String::class.java

                            ) ?: ""
                        val phone_number1 =
                            snapshot.child(phoneNumber).child("phone_number").getValue(
                                String::class.java
                            ) ?: ""
                        val id1 = snapshot.child(phoneNumber).child("id").getValue(
                            String::class.java
                        ) ?: ""
                        val name1 = snapshot.child(phoneNumber).child("name").getValue(
                            String::class.java
                        ) ?: ""
                        val user_created1 =
                            snapshot.child(phoneNumber).child("user_created").getValue(
                                String::class.java
                            ) ?: ""

                        //save data to user
                        userData= User(deviceToken1,phone_number1,id1,name1,user_created1)



                        //create session
                        writeNewUserToSeesion(
                            userData

                        )

                        //move to second page
                        Toast.makeText(activity, "Welcome $name1", Toast.LENGTH_SHORT).show()
                        moveToHomeActivity()
                    } else {

                        //new user not register
                        updateUi()
                        sendVerficationCodeToUser(phoneNumber)
                        Toast.makeText(activity, "code sent", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(activity, error.message, Toast.LENGTH_SHORT).show()
                }
            })
        })

        phoneAuthBinding!!.resendTv.setOnClickListener {
            resendVerificationCode(
                phoneNumber,
                mResendToken
            )
        }

        mCallbacks = object : OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:$credential")
                mVerificationInProgress = false
                signInWithPhoneAuthCredential(credential)

                // Update the UI and attempt sign in with the phone credential
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e)
                mVerificationInProgress = false
                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    phoneAuthBinding!!.textView2.text = "Invalid phone number."
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Snackbar.make(
                        phoneAuthBinding!!.textView, "Quota exceeded.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onCodeSent(
                verificationId: String,
                token: ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:$verificationId")

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId
                mResendToken = token

                // Update UI
            }
        }

        phoneAuthBinding!!.btnVerify.setOnClickListener {

            codeByUSer = phoneAuthBinding!!.firstPinView.text.toString()

            verifyPhoneNumberWithCode(mVerificationId, codeByUSer!!)
        }
    }



    private fun sendVerficationCodeToUser(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(firbaseRepoInstance!!.mAuth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity!!) // Activity (for callback binding)
            .setCallbacks(mCallbacks!!) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun resendVerificationCode(phoneNumber: String, token: ForceResendingToken?) {
        val options = PhoneAuthOptions.newBuilder(firbaseRepoInstance!!.mAuth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity!!) // Activity (for callback binding)
            .setCallbacks(mCallbacks!!) // OnVerificationStateChangedCallbacks
            .setForceResendingToken(token!!) // ForceResendingToken from callbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        firbaseRepoInstance!!.mAuth.signInWithCredential(credential)
            .addOnCompleteListener(activity!!) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    Toast.makeText(activity, "sign In success", Toast.LENGTH_SHORT).show()
                    user = task.result.user!!
                    userId = user!!.uid
                    userCreated = user!!.metadata!!.creationTimestamp.toString()


                    // register this user data in the database
                    deviceToken?.let {

                        firstUserData = User(
                            it,
                            phoneNumber,
                            userId!!,
                            "ahmed",
                            userCreated!!)

                        writeNewUserToDataBase(firstUserData)
                    }

                    // register in seesion manager
                    writeNewUserToSeesion(
                       firstUserData

                    )

                    // move to the next page
                    moveToHomeActivity()
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(activity, "Invalid code ", Toast.LENGTH_SHORT).show()
                    backToLoginFragment()
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        phoneAuthBinding!!.textView2.error = "Invalid code."
                    }
                }
            }
    }

    private fun signOut() {
        firbaseRepoInstance!!.mAuth.signOut()
        backToLoginFragment()
    }

    private fun validatePhoneNumber(): Boolean {
        if (TextUtils.isEmpty(phoneAuthBinding!!.phoneNumEdt.text.toString())) {
            phoneAuthBinding!!.textView.error = "Invalid phone number."
            return false
        }
        return true
    }

    private fun updateUi() {
        phoneAuthBinding?.apply {
            countryCodePicker.visibility = View.INVISIBLE
            phoneNumEdt.visibility = View.INVISIBLE
            textView.visibility = View.INVISIBLE
            textView2.visibility = View.VISIBLE
            firstPinView.visibility = View.VISIBLE
            textView3.visibility = View.VISIBLE
            resendTv.visibility = View.VISIBLE
            textView2.text = "ستصلك رساله نصيه برمز التحقق "
            btnContinue.visibility = View.GONE
            btnVerify.visibility = View.VISIBLE

        }


    }

    //if sign in faild
    private fun backToLoginFragment() {
        val navController = findNavController(view!!)
        navController.navigate(R.id.action_phoneAuthFragment_to_loginFragment)
    }

    // if sign in succes or if the user is already register
    private fun moveToHomeActivity() {
        val intent = Intent(activity, HomeActivity::class.java)
        startActivity(intent)
        activity!!.finish()
    }

    //store userData in user object and in database  //
    private fun writeNewUserToDataBase(
        user: User
    ) {
        databaseReference!!.child(phoneNumber).setValue(user)
    }

    private fun writeNewUserToSeesion(
        user :User
    ) {

        seesionManager!!.createSeesion(
            user
        )
    }

    companion object {
        private val TAG = "PhoneAuthFragment"
    }
}