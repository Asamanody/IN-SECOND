package com.example.insecondapp.ui.auth

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.insecondapp.models.User
import com.example.insecondapp.repos.SessionManager
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.firebase.auth.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class AuthViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    val auth: FirebaseAuth,
    val database: FirebaseDatabase,
    val firebaseMessaging: FirebaseMessaging
) : ViewModel() {

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean>
        get() = _navigateToHome

    // i will get this from the fragment
    var selected_country_code = "+91"

    // i will get this from the fragment
    var phoneNumber = "999999999"

    // i will get this from the fragment
    var codeByUSer: String? = null


    private var mVerificationInProgress = false
    private var mVerificationId: String? = null
    private lateinit var mResendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    val _userDetails = MutableLiveData<HashMap<String, String?>>()
    val userDetails = _userDetails
    val isLogin = sessionManager.isLogin()

    // i can get just the user and get all this data in the view
    // get this data from sign in method
    private val  _user = MutableLiveData <FirebaseUser>()
    val user: LiveData<FirebaseUser>
        get() = _user

    // get this data from sign in method
    private val _userId= MutableLiveData<String>()
    val userId: LiveData<String>
        get() = _userId

    // get this data from sign in method
    private  val _userCreated = MutableLiveData<String>()
    val userCreated: LiveData<String>
        get() = _userCreated

    //device token will store it in the database
    private val _deviceToken = MutableLiveData<String>()
    val deviceToken: LiveData<String>
        get() = _deviceToken

    private val databaseReference = database.getReference("users")



    // function to get device token from firebase
     fun getDeviceToken() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                firebaseMessaging.getToken().addOnSuccessListener {
                    _deviceToken.value = it
                }
            }
        }

    }

    val check_user = databaseReference.orderByChild("phone_number").equalTo(phoneNumber)

    //function to check if user is already registered
    fun checkUser() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                check_user.addListenerForSingleValueEvent(object :
                    com.google.firebase.database.ValueEventListener {
                    override fun onCancelled(p0: com.google.firebase.database.DatabaseError) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataChange(p0: com.google.firebase.database.DataSnapshot) {
                        if (p0.exists()) {
                            _userCreated.value = "user_exists"
                        } else {
                            _userCreated.value = "user_not_exists"
                        }
                    }
                })
            }
        }
    }



    init {
        getUserDetails()
    }

//send verification code to user

    fun createSeesion(user: User) = viewModelScope.launch(Dispatchers.IO)
    {
        sessionManager.createSeesion(user)
    }

    fun getUserDetails() = viewModelScope.launch(Dispatchers.IO) {
        val user = sessionManager.getUserDetails()
        withContext(Dispatchers.Main) { _userDetails.value = user }

    }
    }



